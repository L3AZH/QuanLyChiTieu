package com.example.quanlychitieu.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ServerAPI {
    @POST("auth/login")
    suspend fun login(@Body account:LoginRequest):Response<LoginResponseSuccess>
    @POST("auth/register")
    suspend fun  register(@Body createAcc:RegisterRequest):Response<RegisterResponseSuccess>
    @GET("account/me")
    suspend fun getInfoAccount(
        @Header("Authorization")
        token:String
    ):Response<GetInfoCurrentUserResponseSuccess>
}