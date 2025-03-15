package com.example.siteprogress

import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.siteprogress.databinding.ActivityAdminPortalBinding
import com.example.siteprogress.fragments.*
import com.google.android.material.navigation.NavigationView

class AdminPortal : AppCompatActivity() {

    private lateinit var binding: ActivityAdminPortalBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge() // ✅ Ensure it's called before inflating layout
        binding = ActivityAdminPortalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.toolbar)

        // Set toolbar as ActionBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Set default fragment
        if (savedInstanceState == null) {
            replaceFragment(DashboardFragment())
            navigationView.setCheckedItem(R.id.nav_task_scheduler)
        }

        // Handle Navigation Item Clicks
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_task_scheduler -> replaceFragment(TaskSchedulerFragment())
                R.id.nav_material_tracking -> replaceFragment(MaterialTrackingFragment())
                R.id.nav_issue_reports -> replaceFragment(IssueReportFragment())
                R.id.nav_daily_reports -> replaceFragment(DailyReportsFragment())
                R.id.nav_dashboard -> replaceFragment(DashboardFragment())
                R.id.nav_users -> replaceFragment(UserManagementFragment())
                R.id.nav_projects -> replaceFragment(ProjectManagementFragment())
            }
            drawerLayout.closeDrawers() // Close drawer after selection
            true
        }

        // ✅ Handle Back Button (Android 13+ and older versions)
        handleBackPress()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun handleBackPress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                OnBackInvokedCallback {
                    finish() // Close activity when back is pressed
                }
            )
        } else {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish() // Close activity
                }
            })
        }
    }
}
