package com.bitdev.encryptedgallery.models

data class User(
    val uid: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    var gallery: List<String>
)