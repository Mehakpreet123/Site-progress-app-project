package com.example.siteprogress.data.model



data class IssueReport(
    val id: String = "",  // Firestore document ID
    val title: String = "",
    val description: String = "",
    var status: String = ""
)

