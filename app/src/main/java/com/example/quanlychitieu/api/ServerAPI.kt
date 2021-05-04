package com.example.quanlychitieu.api

import retrofit2.Response
import retrofit2.http.*

interface ServerAPI {
    /*
    * Lop "Repository" se lay ra su dung thong qua Lop "RetrofitInstance"
    * */
    @POST("auth/login")
    suspend fun login(@Body account: LoginRequest): Response<LoginResponseSuccess>

    @POST("auth/register")
    suspend fun register(@Body createAcc: RegisterRequest): Response<RegisterResponseSuccess>

    @GET("account/me")
    suspend fun getInfoAccount(@Header("Authorization") token: String): Response<GetInfoCurrentUserResponseSuccess>

    @GET("account/wallet/me-all-wallet")
    suspend fun getListWalletUser(@Header("Authorization") token: String): Response<GetListWalletUserResponseSuccess>

    @GET("wallettype/get-all-wallettype")
    suspend fun getListWalletType(@Header("Authorization") token: String): Response<GetListWalletTypeResponseSuccess>

    @POST("account/wallet/create-wallet")
    suspend fun createNewWallet(
        @Header("Authorization") token: String,
        @Body newWallet: CreateWalletRequest
    ): Response<CreateWalletResponseSuccess>

    @DELETE("account/wallet/delete-wallet/{typeWallet}")
    suspend fun deleteWallet(
        @Header("Authorization") token: String,
        @Path("typeWallet") typeWallet:String
    ):Response<DeleteWalletResponse>

}