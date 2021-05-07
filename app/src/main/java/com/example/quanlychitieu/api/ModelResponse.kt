package com.example.quanlychitieu.api

import com.example.quanlychitieu.db.modeldb.TransType
import com.example.quanlychitieu.db.modeldb.Transaction
import com.example.quanlychitieu.db.modeldb.WalletType
import com.google.gson.annotations.SerializedName
import java.util.*


/**
 * Model Response của API login
 */
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


/**
 * Model Response của API register
 */
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

/**
 * Model Response của API getInforCurrentUser
 */
data class InfoCurrentUserAPIGetInfoCurrentUser(
    val email: String,
    val username: String,
    val phone: String,
    val joindate: String
)
data class DataGetInfoCurrentUserResponseSuccess(
    @SerializedName("infoShow")
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

/**
 * Model Response của API getListWalletUser
 */
//Response success
data class WalletInfo(
    @SerializedName("idWallet")
    val idWallet:String,
    @SerializedName("Account_email")
    val accountEmail:String,
    @SerializedName("amount")
    val amount:String,
    @SerializedName("type")
    val type:String
)

data class ResultGetListWalletUserResponseSuccess(
    @SerializedName("result")
    val reuslt:List<WalletInfo>
)

data class GetListWalletUserResponseSuccess(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: ResultGetListWalletUserResponseSuccess,
    @SerializedName("flag")
    val flag: Boolean
)
//Response Fail
data class ResultGetListWalletUserResponseFail(
    @SerializedName("message")
    val message:String
)
data class GetListWalletUserResponseFail(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: ResultGetListWalletUserResponseFail,
    @SerializedName("flag")
    val flag: Boolean
)
/**
 * Model Response của API getListWalletType
 */
//Response Success
data class ResultGetListWalletTypeResponseSuccess(
    @SerializedName("result")
    /**
     * WalletType được dịnh nghĩa trong modeldb của db package
     */
    val result:List<WalletType>
)
data class GetListWalletTypeResponseSuccess(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: ResultGetListWalletTypeResponseSuccess,
    @SerializedName("flag")
    val flag: Boolean
)
//Response Fail
data class ResultGetListWalletTypeResponseFail(
    @SerializedName("message")
    val message:String
)
data class GetListWalletTypeResponseFail(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: ResultGetListWalletUserResponseFail,
    @SerializedName("flag")
    val flag: Boolean
)
/**
 * Model Response cua API create wallet
 */
//Response Success
data class ObjectResponse(
    @SerializedName("idWallet")
    val idWallet: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("AccountEmail")
    val AccountEmail: String,
    @SerializedName("WalletTypeIdWalletType")
    val idWalletType:String
)
data class DataCreateWalletResponseSuccess(
    @SerializedName("message")
    val message: String,
    @SerializedName("newObject")
    val newObejct:ObjectResponse
)
data class CreateWalletResponseSuccess(
    @SerializedName("code")
    val code:Int,
    @SerializedName("data")
    val data:DataCreateWalletResponseSuccess,
    @SerializedName("flag")
    val flag: Boolean
)
//Response fail
data class DataCreateWalletResponseFail(
    @SerializedName("message")
    val message: String
)
data class CreateWalletResponseFail(
    @SerializedName("code")
    val code:Int,
    @SerializedName("data")
    val data:DataCreateWalletResponseFail,
    @SerializedName("flag")
    val flag: Boolean
)

data class GetAllTransactionSuccess(
    @SerializedName("code")
    val code:Int,
    @SerializedName("data")
    val data:DataGetAllTransactionSuccess,
    @SerializedName("flag")
    val flag: Boolean
)

data class DataGetAllTransactionSuccess(
    @SerializedName("result")
    val result:List<TransInfoResponse>
)

data class GetAllTransactionFail(
    @SerializedName("code")
    val code:Int,
    @SerializedName("data")
    val data:DataGetAllTransactionFail,
    @SerializedName("flag")
    val flag: Boolean
)

data class DataGetAllTransactionFail(
    @SerializedName("message")
    val message:String
)

data class TransInfoResponse(
    @SerializedName("idTransaction")
    val idTransaction:Int,
    @SerializedName("Wallet_idWallet")
    val wallet_idWallet:Int,
    @SerializedName("TransType_idTransType")
    val transType_id:Int,
    @SerializedName("type")
    val type:String,
    @SerializedName("amount")
    val amount:Double,
    @SerializedName("note")
    val note:String,
    @SerializedName("date")
    val date: Date
)

data class UpdateTransactionResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: UpdateTransactionDataResponse,
    @SerializedName("flag")
    val flag: Boolean
)

data class UpdateTransactionDataResponse(
    @SerializedName("message")
    val message:String
)

data class DeleteTransactionResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: UpdateTransactionDataResponse,
    @SerializedName("flag")
    val flag: Boolean
)

data class DeleteTransactionDataResponse(
    @SerializedName("message")
    val message:String
)

data class GetListTransType(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: GetListTransTypeSuccess,
    @SerializedName("flag")
    val flag: Boolean
)

data class GetListTransTypeSuccess(
    @SerializedName("result")


    val result:List<TransType>
)


/**
 * Model Response cua API delete wallet
 */
data class DataDeleteWalletResponse(
    @SerializedName("message")
    val message: String
)
data class DeleteWalletResponse(
    @SerializedName("code")
    val code:Int,
    @SerializedName("data")
    val data:DataDeleteWalletResponse,
    @SerializedName("flag")
    val flag: Boolean
)

/**
 * Model Response cua API update wallet
 */
data class DataUpdateWalletResponse(
    @SerializedName("message")
    val message: String
)
data class UpdateWalletResponse(
    @SerializedName("code")
    val code:Int,
    @SerializedName("data")
    val data:DataUpdateWalletResponse,
    @SerializedName("flag")
    val flag: Boolean
)
