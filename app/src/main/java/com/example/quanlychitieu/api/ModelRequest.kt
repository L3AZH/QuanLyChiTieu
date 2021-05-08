package com.example.quanlychitieu.api

import com.google.gson.annotations.SerializedName
import java.sql.Date

/**
 * Request body cua login API
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String)
/**
 * Request body cua Register API
 */
data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    val phone: String
)
/**
 * Request body cua create wallet API
 */
data class CreateWalletRequest(
    val amount:String,
    val idWalletType:String
)
/**
 * Request body cua update wallet API
 */
data class UpdateWalletRequest(
    val amount:String,
)

data class UpdateTransactionRequest(
    val idTransaction:Int,
    val idTransType:Int,
    val amount:Double,
    val note:String
)

data class CreateTransactionRequest(
    val wallet_idWallet:Int,
    val idTransType:Int,
    val amount:Double,
    val note:String,
    val date:Date
)
