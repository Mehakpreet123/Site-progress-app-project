package com.example.siteprogress.data.model

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val deadline: String = "",
    val projectId: String = "",  // ✅ Links task to a project
    var isCompleted: Boolean = false
)
