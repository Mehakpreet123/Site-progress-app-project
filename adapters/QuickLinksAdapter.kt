package com.example.siteprogress.adapters



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.siteprogress.R
import com.example.siteprogress.data.model.QuickAction



class QuickLinksAdapter(private val quickLinks: List<QuickAction>) :
    RecyclerView.Adapter<QuickLinksAdapter.QuickLinkViewHolder>() {

    class QuickLinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnQuickLink: Button = itemView.findViewById(R.id.btnQuickLink)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickLinkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quick_link, parent, false)
        return QuickLinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuickLinkViewHolder, position: Int) {
        val quickLink = quickLinks[position]
        holder.btnQuickLink.text = quickLink.name

        // Ensure onClick is not null before calling it
        holder.btnQuickLink.setOnClickListener {
            quickLink.onClick?.invoke() // Safe call to avoid null pointer exception
        }
    }


    override fun getItemCount() = quickLinks.size
}
