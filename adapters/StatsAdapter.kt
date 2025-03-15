package com.example.siteprogress.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.R
import com.example.siteprogress.data.model.Stat

class StatsAdapter(private var stats: List<Stat>) :
    RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    class StatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconImageView: ImageView = view.findViewById(R.id.imgStatIcon)
        val titleTextView: TextView = view.findViewById(R.id.tvStatTitle)
        val countTextView: TextView = view.findViewById(R.id.tvStatCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stat_card, parent, false)
        return StatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        val stat = stats[position]
        holder.titleTextView.text = stat.title
        holder.countTextView.text = stat.value
        holder.iconImageView.setImageResource(stat.iconResId) // Set icon dynamically
    }

    override fun getItemCount(): Int = stats.size

    fun updateStats(newStats: List<Stat>) {
        stats = newStats
        notifyDataSetChanged()
    }
}
