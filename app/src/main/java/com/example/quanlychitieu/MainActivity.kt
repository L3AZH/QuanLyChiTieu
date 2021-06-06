package com.example.quanlychitieu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.quanlychitieu.databinding.ActivityMainBinding
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.LoginRegister.LoginAndRegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val sharePreference =
            getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharePreference.getString("accountToken", "")
        CoroutineScope(Dispatchers.Default).launch {
            if (token.isNullOrBlank()) {
                val gotoLoginAndRegisterActivity =
                    Intent(this@MainActivity, LoginAndRegisterActivity::class.java)
                delay(2000)
                startActivity(gotoLoginAndRegisterActivity)
            }
            else{
                val gotoHomeActivity = Intent(this@MainActivity,HomeActivity::class.java)
                delay(2000)
                startActivity(gotoHomeActivity)
            }
        }
    }
}