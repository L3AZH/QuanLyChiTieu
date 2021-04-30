package com.example.quanlychitieu.api

import com.google.gson.annotations.SerializedName
import java.util.*


//Model Response của API login
//Response Success
data class DataLoginResponseSuccess(
    @SerializedName("message")
    val message: String,
    @SerializedName("token")
    val token:String
)

data class LoginResponseSuccess(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: DataLoginResponseSuccess,
    @SerializedName("flag")
    val flag: Boolean
)
//Response Fail
data class DataLoginResponseFail(
    @SerializedName("message")
    val message: String,
)

data class LoginResponseFail(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: DataLoginResponseFail,
    @SerializedName("flag")
    val flag: Boolean
)



//Model Response của API register
//Reponse Fail

data class DataRegisterResponseFail(
    @SerializedName("message")
    val message: String
)

data class RegisterResponseFail(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: DataRegisterResponseFail,
    @SerializedName("flag")
    val flag: Boolean
)
//Reponse Success
data class InfoNewAccountResponseRegisterSuccess(
    val email: String,
    val username:String,
    val phone:String,
    val joindate:String
)
data class DataRegisterResponseSuccess(
    @SerializedName("message")
    val message: String,
    @SerializedName("newObject")
    val newObject: InfoNewAccountResponseRegisterSuccess
)
data class RegisterResponseSuccess(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: DataRegisterResponseSuccess,
    @SerializedName("flag")
    val flag: Boolean
)

//Model Response của API getInforCurrentUser
data class InfoCurrentUserAPIGetInfoCurrentUser(
    val email: String,
    val username: String,
    val phone: String,
    val joindate: String
)
data class DataGetInfoCurrentUserResponseSuccess(
    @SerializedName("showInfo")
    val infoShow : InfoCurrentUserAPIGetInfoCurrentUser
)
data class GetInfoCurrentUserResponseSuccess(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: DataGetInfoCurrentUserResponseSuccess,
    @SerializedName("flag")
    val flag: Boolean
)
