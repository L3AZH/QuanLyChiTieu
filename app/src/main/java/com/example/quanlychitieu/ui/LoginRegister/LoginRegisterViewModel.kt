package com.example.quanlychitieu.ui.LoginRegister

import androidx.lifecycle.ViewModel
import com.example.quanlychitieu.api.LoginRequest
import com.example.quanlychitieu.api.LoginResponseFail
import com.example.quanlychitieu.api.RegisterRequest
import com.example.quanlychitieu.repository.Repository
import com.example.quanlychitieu.api.RegisterResponseFail
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.util.*


class LoginRegisterViewModel(val repository: Repository) : ViewModel() {


    suspend fun loginAction(account: LoginRequest):Deferred<Array<String>> = CoroutineScope(Dispatchers.Default).async{
        var response = repository.login(account)
        if (response.isSuccessful()){
            val body = response.body()!!
            var resultList = arrayOf(body.code.toString(),body.data.token)
            resultList
            //lưu ý cái result khi dem di split string
            /*
            * ban tuan anh ngu lon da viet nhu sau:
            * body.code.toString()+"-"+body.data.message+" - "+body.data.token =>
            * khi tach chuoi (tach theo dau "-")thi token se nhan dc ve string nhu sau "<khoang cach>token"
            * va dung string token tren de dua vao header thi header se thanh nhu sau:
            * Bearer:<khoang cach><khoang cach>token
            * sau do server se tach theo khoang cach => string[0] = Bear
            * !!!String[1] = "<khoang cach>token" => loi unthorization vi sai token
            * */
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

    suspend fun registerAction(account: RegisterRequest):Deferred<String> = CoroutineScope(Dispatchers.Default).async {
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
}