package com.example.quanlychitieu.ui.LoginRegister.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.quanlychitieu.R
import com.example.quanlychitieu.api.LoginRequest
import com.example.quanlychitieu.databinding.FragmentLoginBinding
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.LoginRegister.LoginAndRegisterActivity
import com.example.quanlychitieu.ui.LoginRegister.LoginRegisterViewModel
import com.google.android.material.snackbar.Snackbar
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks
import kotlinx.coroutines.*


class LoginFragment : Fragment() {

    lateinit var binding:FragmentLoginBinding
    lateinit var viewModel: LoginRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        viewModel = (activity as LoginAndRegisterActivity).loginRegisterViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLinkForText()
        setOnclickLogin()
    }

    fun setLinkForText(){
        val linkRegister = Link("Register")
            .setTextColor(Color.BLACK)
            .setBold(true)
            .setUnderlined(false)
            .setOnClickListener {
                val gotoFragmentRegister = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(gotoFragmentRegister)
            }
        binding.registerTextView.applyLinks(linkRegister)
    }
    fun setOnclickLogin(){
        binding.loginBtn.setOnClickListener {
            if (checkEmptyInputField()){
                CoroutineScope(Dispatchers.Default).launch {
                    val resultString = viewModel.loginAction(LoginRequest(
                        binding.emailInputEditText.text.toString()
                        ,binding.passwordEditText.text.toString())).await()
                    if(resultString[0].equals("200")){
                        val sharePreference =
                            requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
                        Log.i("token get", "setOnclickLogin: "+resultString[1])
                        sharePreference.edit().putString("accountToken","Bearer ${resultString[1].trim()}").apply()
                        val gotoHomeActivity = Intent(context,HomeActivity::class.java)
                        startActivity(gotoHomeActivity)
                    }
                    else{
                        Snackbar.make(binding.root,resultString[1],Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    //validation
    fun checkEmptyInputField():Boolean{
        var flag = true
        if(binding.emailInputEditText.text.toString().trim().isEmpty()) {
            binding.emailTextInputLayout.error = "Email is empty"
            flag = false
        }
        else{
            binding.emailTextInputLayout.error = ""
            flag = true
        }
        if(binding.passwordEditText.text.toString().trim().isEmpty()){
            binding.PasswordTextInputLayout.error = "password is empty"
            flag = false
        }
        else{
            binding.PasswordTextInputLayout.error = ""
            flag = true
        }
        return flag
    }
}