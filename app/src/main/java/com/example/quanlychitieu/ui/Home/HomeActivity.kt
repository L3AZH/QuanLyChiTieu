package com.example.quanlychitieu.ui.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.quanlychitieu.R
import com.example.quanlychitieu.databinding.ActivityHomeBinding
import com.example.quanlychitieu.repository.Repository

class HomeActivity : AppCompatActivity() {

    lateinit var binding:ActivityHomeBinding
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)

        val repository = Repository()
        val homeViewModelFactory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this,homeViewModelFactory).get(HomeViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home_container)
        binding.bottomNavigationView.setupWithNavController(navHostFragment!!.findNavController())
    }
}