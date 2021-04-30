package com.example.quanlychitieu.ui.LoginRegister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quanlychitieu.repository.Repository

class LoginRegisterViewModelFactory(val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginRegisterViewModel::class.java)){
            return LoginRegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow View Model")
    }
}