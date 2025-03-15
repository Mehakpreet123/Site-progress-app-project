package com.example.siteprogress

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.adapters.ReportsAdapter
import com.example.siteprogress.data.model.Report
import com.google.firebase.firestore.FirebaseFirestore

class DailyReportsFragment : Fragment(), ReportsAdapter.OnReportActionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var reportsAdapter: ReportsAdapter
    private val reportsList = mutableListOf<Report>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_daily_reports, container, false)

        recyclerView = view.findViewById(R.id.reportsRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        reportsAdapter = ReportsAdapter(reportsList, this)
        recyclerView.adapter = reportsAdapter

        fetchReports()
        return view
    }

    private fun fetchReports() {
        progressBar.visibility = View.VISIBLE
        db.collection("reports")
            .whereEqualTo("status", "Pending")
            .get()
            .addOnSuccessListener { documents ->
                reportsList.clear()
                for (document in documents) {
                    val report = document.toObject(Report::class.java)
                    report.id = document.id  // 🔹 Store Firestore document ID in Report object
                    reportsList.add(report)
                }
                reportsAdapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Failed to load reports", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onApprove(report: Report) {
        updateReportStatus(report, "Approved")
    }

    override fun onReject(report: Report) {
        updateReportStatus(report, "Rejected")
    }

    private fun updateReportStatus(report: Report, newStatus: String) {
        if (report.id.isNullOrEmpty()) {
            Log.e("DailyReports", "Error: Report ID is empty")
            return
        }

        val reportRef = db.collection("reports").document(report.id) // ✅ Use correct Firestore document ID

        reportRef.update("status", newStatus)
            .addOnSuccessListener {
                Log.d("DailyReports", "Report ${report.id} updated to $newStatus")
                Toast.makeText(requireContext(), "Report updated", Toast.LENGTH_SHORT).show()
                fetchReports() // Refresh list after update
            }
            .addOnFailureListener { e ->
                Log.e("DailyReports", "Error updating report", e)
                Toast.makeText(requireContext(), "Failed to update report", Toast.LENGTH_SHORT).show()
            }
    }
}
