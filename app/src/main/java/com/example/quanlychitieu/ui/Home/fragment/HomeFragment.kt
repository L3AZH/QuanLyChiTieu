package com.example.quanlychitieu.ui.Home.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.quanlychitieu.R
import com.example.quanlychitieu.databinding.FragmentHomeBinding
import com.example.quanlychitieu.dialog.LoadingDialog
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.example.quanlychitieu.ui.LoginRegister.LoginAndRegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    lateinit var binding:FragmentHomeBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        viewModel = (activity as HomeActivity).homeViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInfoUser()
        setOnClickInfoBtn()
    }
    fun setUpInfoUser(){
        val sharePreference =
            requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharePreference.getString("accountToken", "null")
        viewModel.infoUser.observe(viewLifecycleOwner, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                binding.emailAccountTextView.text = "Email: "+viewModel.infoUser.value?.data?.infoShow?.email
                binding.usernameAccountTextView.text = "User name: "+viewModel.infoUser.value?.data?.infoShow?.username
                binding.phoneAccountTextView.text = "SDT: "+viewModel.infoUser.value?.data?.infoShow?.phone
                binding.joindateAccountTextView.text = "Ngay tao: "+viewModel.infoUser.value?.data?.infoShow?.joindate
            }
        })
        viewModel.setInfoUser(token!!)
    }
    fun setOnClickInfoBtn(){
        binding.infoImgViewBtn.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.cardViewInfo,AutoTransition())
            if(binding.emailAccountTextView.isVisible){
                binding.usernameAccountTextView.visibility = View.GONE
                binding.emailAccountTextView.visibility = View.GONE
                binding.phoneAccountTextView.visibility = View.GONE
                binding.joindateAccountTextView.visibility = View.GONE
                binding.linearLayoutBtnInfo.visibility = View.GONE
            }
            else{
                binding.usernameAccountTextView.visibility = View.VISIBLE
                binding.emailAccountTextView.visibility = View.VISIBLE
                binding.phoneAccountTextView.visibility = View.VISIBLE
                binding.joindateAccountTextView.visibility = View.VISIBLE
                binding.linearLayoutBtnInfo.visibility = View.VISIBLE
            }
        }
        binding.homeLogoutBtn.setOnClickListener {
            val sharePreference =
                requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
            sharePreference.edit().putString("accountToken",null).apply()
            val goToLoginScreen = Intent(activity,LoginAndRegisterActivity::class.java)
            startActivity(goToLoginScreen)
        }
    }
}