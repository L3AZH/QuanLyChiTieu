package com.example.quanlychitieu.api

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String)

data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    val phone: String
)
