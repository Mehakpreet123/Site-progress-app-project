package com.example.siteprogress.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.R
import com.example.siteprogress.data.model.Report

class ReportsAdapter(private val reports: List<Report>, private val listener: OnReportActionListener) :
    RecyclerView.Adapter<ReportsAdapter.ReportViewHolder>() {

    interface OnReportActionListener {
        fun onApprove(report: Report)
        fun onReject(report: Report)
    }

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectName: TextView = itemView.findViewById(R.id.tvProjectName)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val btnApprove: Button = itemView.findViewById(R.id.btnApprove)
        val btnReject: Button = itemView.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]
        holder.projectName.text = report.title
        holder.description.text = report.description

        holder.btnApprove.setOnClickListener { listener.onApprove(report) }
        holder.btnReject.setOnClickListener { listener.onReject(report) }
    }

    override fun getItemCount(): Int = reports.size
}
