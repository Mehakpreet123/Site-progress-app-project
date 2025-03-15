package com.example.siteprogress.data.model

data class Report(
    var id: String = "",
    var title: String = "",
    val description: String = "",
    val status: String = "Pending" // Pending, Approved, Rejected
)

