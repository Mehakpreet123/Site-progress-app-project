package com.example.siteprogress
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.adapters.MaterialAdapter
import com.example.siteprogress.data.model.MaterialRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class MaterialTrackingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var materialAdapter: MaterialAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_material_tracking, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewMaterials)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        materialAdapter = MaterialAdapter(emptyList(), ::approveRequest, ::rejectRequest)
        recyclerView.adapter = materialAdapter

        fetchMaterialRequests()

        return view
    }

    private fun fetchMaterialRequests() {
        firestore.collection("material_requests")
            .get(Source.SERVER)
            .addOnSuccessListener { documents ->
                val requests = documents.map { doc ->
                    MaterialRequest(
                        id = doc.id,
                        materialName = doc.getString("materialName") ?: "",
                        quantity = doc.getLong("quantity")?.toInt() ?: 0,
                        requestedBy = doc.getString("requestedBy") ?: "",
                        status = doc.getString("status") ?: "Pending"
                    )
                }
                materialAdapter.updateRequests(requests)
            }
    }

    private fun approveRequest(request: MaterialRequest) {
        updateRequestStatus(request, "Approved")
    }

    private fun rejectRequest(request: MaterialRequest) {
        updateRequestStatus(request, "Rejected")
    }

    private fun updateRequestStatus(request: MaterialRequest, status: String) {
        firestore.collection("material_requests").document(request.id)
            .update("status", status)
            .addOnSuccessListener {
                fetchMaterialRequests() // Refresh the list
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error updating request status: ${e.message}")
            }
    }
}
