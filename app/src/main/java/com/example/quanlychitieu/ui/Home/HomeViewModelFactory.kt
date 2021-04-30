package com.example.quanlychitieu.ui.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quanlychitieu.repository.Repository

class HomeViewModelFactory(val repository: Repository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow View Model")
    }
}