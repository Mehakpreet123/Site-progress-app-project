package com.example.siteprogress.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.R
import com.example.siteprogress.data.model.ActivityItem

class ActivityAdapter(private var activities: List<ActivityItem>) :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descriptionTextView: TextView = view.findViewById(R.id.activityDescription)
        val timestampTextView: TextView = view.findViewById(R.id.activityTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.descriptionTextView.text = activity.description
        holder.timestampTextView.text = activity.timestamp
    }

    override fun getItemCount(): Int = activities.size

    // 🔹 Update the activity list dynamically
    fun updateActivities(newActivities: List<ActivityItem>) {
        activities = newActivities
        notifyDataSetChanged()
    }
}
