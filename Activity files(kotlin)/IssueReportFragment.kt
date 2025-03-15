package com.example.siteprogress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.adapters.IssueAdapter
import com.example.siteprogress.data.model.IssueReport
import com.google.firebase.firestore.FirebaseFirestore

class IssueReportFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var issueAdapter: IssueAdapter
    private val issueList = mutableListOf<IssueReport>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_issue_reports, container, false)

        recyclerView = view.findViewById(R.id.rvIssueReports)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        issueAdapter = IssueAdapter(issueList, this::updateIssueStatus, this::deleteIssue)
        recyclerView.adapter = issueAdapter

        fetchIssueReports() // Load issues from Firestore

        return view
    }

    private fun fetchIssueReports() {
        db.collection("issues")
            .get()
            .addOnSuccessListener { documents ->
                issueList.clear()
                for (document in documents) {
                    val issue = document.toObject(IssueReport::class.java).copy(id = document.id)
                    issueList.add(issue)
                }
                issueAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                println("Error fetching issues: $e")
            }
    }

    fun updateIssueStatus(issueId: String, position: Int) {
        db.collection("issues").document(issueId)
            .update("status", "Resolved")
            .addOnSuccessListener {
                issueList[position].status = "Resolved"
                issueAdapter.notifyItemChanged(position)
            }
            .addOnFailureListener { e ->
                println("Error updating issue status: $e")
            }
    }

    fun deleteIssue(issueId: String, position: Int) {
        db.collection("issues").document(issueId)
            .delete()
            .addOnSuccessListener {
                issueList.removeAt(position)
                issueAdapter.notifyItemRemoved(position)
                issueAdapter.notifyItemRangeChanged(position, issueList.size)
            }
            .addOnFailureListener { e ->
                println("Error deleting issue: $e")
            }
    }
}
