package com.example.quanlychitieu.ui.LoginRegister

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.quanlychitieu.api.LoginRequest
import com.example.quanlychitieu.api.LoginResponseFail
import com.example.quanlychitieu.api.RegisterRequest
import com.example.quanlychitieu.repository.Repository
import com.example.quanlychitieu.api.RegisterResponseFail
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.util.*


class LoginRegisterViewModel(val repository: Repository) : ViewModel() {


    suspend fun loginAction(account: LoginRequest):Deferred<Array<String>> = CoroutineScope(Dispatchers.Default).async{
        try{
            var response = repository.login(account)
            if (response.isSuccessful()){
                val body = response.body()!!
                var resultList = arrayOf(body.code.toString(),body.data.token)
                resultList
            }
            else{
                val gson = Gson()
                val loginResponseFail = gson.fromJson(
                    response.errorBody()!!.string(),
                    LoginResponseFail::class.java
                )
                var resultList:Array<String> = arrayOf(loginResponseFail.code.toString(),loginResponseFail.data.message)
                resultList
            }
        }
        catch (error:SocketTimeoutException){
            error.printStackTrace()
            var resultList = arrayOf("Connection time out....")
            resultList
        }
    }

    suspend fun registerAction(account: RegisterRequest):Deferred<String> = CoroutineScope(Dispatchers.Default).async {
        try{
            var response = repository.register(account)
            if (response.isSuccessful()){
                val body = response.body()!!
                val result = body.data.message+"-"+
                        body.data.newObject.email+"-"+
                        body.data.newObject.username+"-"+
                        body.data.newObject.phone+"-"+
                        body.data.newObject.joindate
                result
            }
            else{
                val gson = Gson()
                val registerResponseFail = gson.fromJson(
                    response.errorBody()!!.string(),
                    RegisterResponseFail::class.java)
                registerResponseFail.data.message
            }
        }
        catch (error:SocketTimeoutException){
            error.printStackTrace()
            "Connection time out...."
        }
    }
}