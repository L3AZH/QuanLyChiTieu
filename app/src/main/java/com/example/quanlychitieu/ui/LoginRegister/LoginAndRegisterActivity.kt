package com.example.quanlychitieu.ui.LoginRegister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quanlychitieu.R
import com.example.quanlychitieu.databinding.ActivityLoginAndRegisterBinding
import com.example.quanlychitieu.db.DatabaseInstance
import com.example.quanlychitieu.repository.Repository

class LoginAndRegisterActivity : AppCompatActivity() {

    lateinit var binding:ActivityLoginAndRegisterBinding
    lateinit var loginRegisterViewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login_and_register)
        val dao = DatabaseInstance(this).getDbDao()
        val loginRegisterRepository = Repository(dao)
        val loginRegisterViewModelFactory = LoginRegisterViewModelFactory(loginRegisterRepository)
        loginRegisterViewModel = ViewModelProvider(this,loginRegisterViewModelFactory).get(LoginRegisterViewModel::class.java)
        binding.lifecycleOwner = this
    }
}