package com.example.quanlychitieu.ui.Home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.quanlychitieu.R
import com.example.quanlychitieu.databinding.ActivityHomeBinding
import com.example.quanlychitieu.db.DatabaseInstance
import com.example.quanlychitieu.dialog.LoadingDialog
import com.example.quanlychitieu.repository.Repository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeActivity : AppCompatActivity() {

    lateinit var binding:ActivityHomeBinding
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)

        val dao = DatabaseInstance(this).getDbDao()
        val repository = Repository(dao)
        val homeViewModelFactory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this,homeViewModelFactory).get(HomeViewModel::class.java)
        loadingDataFromServerToDatabase()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home_container)
        binding.bottomNavigationView.setupWithNavController(navHostFragment!!.findNavController())
    }


    fun loadingDataFromServerToDatabase(){
        val sharePreference = getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharePreference.getString("accountToken", "null")
        CoroutineScope(Dispatchers.Default).launch {
            var result1 = homeViewModel.loadingListWalletTypeFromSvToDb(token!!).await()
            var result2 = homeViewModel.getListTransTypeFromServer(token!!).await()
            println(result2)
            if(result1 && result2){
                Snackbar.make(binding.root, "load data success", Snackbar.LENGTH_LONG).show()
            }
            else{
                Snackbar.make(binding.root, "load data fail", Snackbar.LENGTH_LONG).show()
            }
        }
    }
    
}