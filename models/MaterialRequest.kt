package com.example.siteprogress.data.model



data class MaterialRequest(
    val id: String = "",
    val materialName: String = "",
    val quantity: Int = 0,
    val requestedBy: String = "",
    val status: String = "Pending"
)
