package com.example.siteprogress.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.R
import com.example.siteprogress.data.model.Task

class TasksAdapter(
    var tasks: List<Task>,
    private val listener: OnTaskActionListener
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    interface OnTaskActionListener {
        fun onMarkComplete(task: Task)
        fun onEditTask(task: Task)
        fun onDeleteTask(task: Task)
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtTaskTitle)
        val description: TextView = view.findViewById(R.id.txtTaskDescription)
        val deadline: TextView = view.findViewById(R.id.txtTaskDeadline)
        val btnComplete: Button = view.findViewById(R.id.btnMarkComplete)
        val btnEdit: Button = view.findViewById(R.id.btnEditTask)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.description.text = task.description
        holder.deadline.text = "Deadline: ${task.deadline}"

        // Reset button state before binding new data
        holder.btnComplete.isEnabled = true
        holder.btnComplete.text = "Mark Complete"

        // ✅ If task is completed, update UI correctly
        if (task.isCompleted) {
            holder.btnComplete.text = "Completed"
            holder.btnComplete.isEnabled = false
        }

        holder.btnComplete.setOnClickListener { listener.onMarkComplete(task) }
        holder.btnEdit.setOnClickListener { listener.onEditTask(task) }
        holder.btnDelete.setOnClickListener { listener.onDeleteTask(task) }
    }

    override fun getItemCount(): Int = tasks.size

    // ✅ New function to update tasks dynamically
    fun updateTasks(newTasks: List<Task>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = tasks.size
            override fun getNewListSize() = newTasks.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return tasks[oldItemPosition].id == newTasks[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return tasks[oldItemPosition] == newTasks[newItemPosition]
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        tasks = newTasks.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

}
