package com.example.siteprogress.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.R
import com.example.siteprogress.data.model.MaterialRequest

class MaterialAdapter(
    private var materialRequests: List<MaterialRequest> = listOf(),
    private val onApproveClick: (MaterialRequest) -> Unit,
    private val onRejectClick: (MaterialRequest) -> Unit
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    // ViewHolder class
    class MaterialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtMaterialName: TextView = view.findViewById(R.id.txtMaterialName)
        val txtQuantity: TextView = view.findViewById(R.id.txtQuantity)
        val txtRequestedBy: TextView = view.findViewById(R.id.txtRequestedBy)
        val txtStatus: TextView = view.findViewById(R.id.txtStatus)
        val btnApprove: Button = view.findViewById(R.id.btnApprove)
        val btnReject: Button = view.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_material_request, parent, false)
        return MaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val request = materialRequests[position]

        holder.txtMaterialName.text = "Material: ${request.materialName}"
        holder.txtQuantity.text = "Quantity: ${request.quantity}"
        holder.txtRequestedBy.text = "Requested By: ${request.requestedBy}"
        holder.txtStatus.text = "Status: ${request.status}"

        // Disable buttons if already approved/rejected
        val isProcessed = request.status != "Pending"
        holder.btnApprove.isEnabled = !isProcessed
        holder.btnReject.isEnabled = !isProcessed

        holder.btnApprove.setOnClickListener { onApproveClick(request) }
        holder.btnReject.setOnClickListener { onRejectClick(request) }
    }

    override fun getItemCount(): Int = materialRequests.size

    // 🔥 Update function (Fixing "updateRequests() not found" issue)
    fun updateRequests(newRequests: List<MaterialRequest>) {
        materialRequests = newRequests.toMutableList()
        notifyDataSetChanged()
    }


}
