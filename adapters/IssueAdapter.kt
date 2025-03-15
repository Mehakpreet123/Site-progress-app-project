package com.example.siteprogress.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.R
import com.example.siteprogress.data.model.IssueReport

class IssueAdapter(
    private val issueList: MutableList<IssueReport>,
    private val updateIssueStatus: (String, Int) -> Unit,
    private val deleteIssue: (String, Int) -> Unit
) : RecyclerView.Adapter<IssueAdapter.IssueViewHolder>() {

    class IssueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvIssueTitle)
        val tvDescription: TextView = view.findViewById(R.id.tvIssueDescription)
        val tvStatus: TextView = view.findViewById(R.id.tvIssueStatus)
        val btnResolve: Button = view.findViewById(R.id.btnResolveIssue)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteIssue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_issue_report, parent, false)
        return IssueViewHolder(view)
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        val issue = issueList[position]

        holder.tvTitle.text = issue.title
        holder.tvDescription.text = issue.description
        holder.tvStatus.text = "Status: ${issue.status}"

        // Show/hide buttons based on status
        if (issue.status == "Resolved") {
            holder.btnResolve.visibility = View.GONE
            holder.btnDelete.visibility = View.VISIBLE
        } else {
            holder.btnResolve.visibility = View.VISIBLE
            holder.btnDelete.visibility = View.GONE
        }

        // Handle Resolve button click
        holder.btnResolve.setOnClickListener {
            updateIssueStatus(issue.id, position)
        }

        // Handle Delete button click
        holder.btnDelete.setOnClickListener {
            deleteIssue(issue.id, position)
        }
    }

    override fun getItemCount(): Int = issueList.size
}
