package com.example.siteprogress.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.R
import com.example.siteprogress.data.model.Project

class ProjectsAdapter(
    private var projectList: List<Project>,
    private val onEditClick: (Project) -> Unit,
    private val onDeleteClick: (Project) -> Unit
) : RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvProjectTitle)
        val status: TextView = view.findViewById(R.id.tvProjectStatus)
        val progressBar: ProgressBar = view.findViewById(R.id.projectProgressBar)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projectList[position]
        holder.title.text = project.name
        holder.status.text = project.status
        holder.progressBar.progress = project.progress

        holder.btnEdit.setOnClickListener { onEditClick(project) }
        holder.btnDelete.setOnClickListener { onDeleteClick(project) }
    }

    override fun getItemCount(): Int = projectList.size

    // ✅ Method to update the project list
    fun updateProjects(newProjects: List<Project>) {
        projectList = newProjects
        notifyDataSetChanged() // Refresh RecyclerView
    }

    // ✅ Add this method to return the current list
    fun getProjects(): List<Project> {
        return projectList
    }
}

