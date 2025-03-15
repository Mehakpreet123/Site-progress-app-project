package com.example.siteprogress.data.model

import com.google.firebase.firestore.IgnoreExtraProperties

/*
data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val phone: String,
    val project: String
)

 */

data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var role: String = "",
    var phone: String = "",
    var project: String = ""
) {
    constructor() : this("", "", "", "", "", "") // No-arg constructor required for Firestore
}

