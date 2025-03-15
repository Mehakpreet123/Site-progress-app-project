package com.example.siteprogress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.siteprogress.adapters.QuickLinksAdapter
import com.example.siteprogress.adapters.StatsAdapter
import com.example.siteprogress.adapters.ActivityAdapter
import com.example.siteprogress.data.model.Stat
import com.example.siteprogress.data.model.QuickAction
import com.example.siteprogress.data.model.ActivityItem

class DashboardFragment : Fragment() {

    private lateinit var statRecyclerView: RecyclerView
    private lateinit var quickActionsRecyclerView: RecyclerView
    private lateinit var recentActivitiesRecyclerView: RecyclerView
    private lateinit var statAdapter: StatsAdapter
    private lateinit var quickActionAdapter: QuickLinksAdapter
    private lateinit var activityAdapter: ActivityAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // Initialize RecyclerViews
        statRecyclerView = view.findViewById(R.id.recyclerViewStats)
        quickActionsRecyclerView = view.findViewById(R.id.recyclerViewQuickActions)
        recentActivitiesRecyclerView = view.findViewById(R.id.recyclerViewRecentActivities)

        // Setup LayoutManagers
        statRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        quickActionsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recentActivitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize Adapters
        statAdapter = StatsAdapter(emptyList())  // Initially empty, data will be fetched
        quickActionAdapter = QuickLinksAdapter(getQuickActions())
        activityAdapter = ActivityAdapter(emptyList())

        // Set Adapters
        statRecyclerView.adapter = statAdapter
        quickActionsRecyclerView.adapter = quickActionAdapter
        recentActivitiesRecyclerView.adapter = activityAdapter

        // Fetch Data from Firebase
        fetchStatsFromFirebase()
        fetchRecentActivities()

        return view
    }

    private fun fetchStatsFromFirebase() {
        db.collection("stats").get().addOnSuccessListener { result ->
            val statsList = mutableListOf<Stat>()
            for (document in result) {
                val title = document.getString("title") ?: "Unknown"
                val count = document.getString("count") ?: "0"
                val icon = when (title) {
                    "Projects" -> R.drawable.ic_projects
                    "Engineers" -> R.drawable.ic_engineer
                    "Tasks" -> R.drawable.ic_task
                    else -> R.drawable.ic_placeholder
                }
                statsList.add(Stat(title, count, icon))
            }
            statAdapter.updateStats(statsList)
        }.addOnFailureListener { exception ->
            Log.e("DashboardFragment", "Error fetching stats: ", exception)
        }
    }

    private fun fetchRecentActivities() {
        db.collection("activities").orderBy("timestamp").limit(10).get()
            .addOnSuccessListener { result ->
                val activitiesList = mutableListOf<ActivityItem>()
                for (document in result) {
                    val title = document.getString("title") ?: "Unknown Activity"
                    val time = document.getString("time") ?: "Unknown Time"
                    activitiesList.add(ActivityItem(title, time))
                }
                activityAdapter.updateActivities(activitiesList)
            }
            .addOnFailureListener { exception ->
                Log.e("DashboardFragment", "Error fetching activities: ", exception)
            }
    }

    private fun getQuickActions(): List<QuickAction> {
        return listOf(
            QuickAction("Users", R.drawable.ic_users),
            QuickAction("Projects", R.drawable.ic_projects),
            QuickAction("Reports", R.drawable.ic_reports),
            QuickAction("Tasks", R.drawable.ic_task),
            QuickAction("Materials", R.drawable.ic_material),
            QuickAction("Issues", R.drawable.ic_issue)
        )
    }
}
