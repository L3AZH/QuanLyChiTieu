package com.example.quanlychitieu.repository

import com.example.quanlychitieu.api.LoginRequest
import com.example.quanlychitieu.api.RegisterRequest
import com.example.quanlychitieu.api.RetrofitInstance
import retrofit2.Response

class Repository {
    /*
    * View Model se lay ham trong day su dung
    * */
    suspend fun login(account:LoginRequest) = RetrofitInstance.api.login(account)
    suspend fun register(account: RegisterRequest) = RetrofitInstance.api.register(account)
    suspend fun getInfoCurrentUser(token:String) = RetrofitInstance.api.getInfoAccount(token)
}