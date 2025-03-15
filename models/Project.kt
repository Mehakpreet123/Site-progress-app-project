package com.example.siteprogress.data.model



data class Project(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val deadline: String = "",
    val status: String = "Not Started", // Possible values: "Not Started", "In Progress", "Completed"
    val progress: Int = 0 // 0 to 100
)
