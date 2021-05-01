package com.example.quanlychitieu.ui.LoginRegister.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.quanlychitieu.R
import com.example.quanlychitieu.api.RegisterRequest
import com.example.quanlychitieu.databinding.FragmentRegisterBinding
import com.example.quanlychitieu.ui.LoginRegister.LoginAndRegisterActivity
import com.example.quanlychitieu.ui.LoginRegister.LoginRegisterViewModel
import com.example.quanlychitieu.ui.LoginRegister.dialog.LoadingDialog
import com.google.android.material.snackbar.Snackbar
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks
import kotlinx.coroutines.*

class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding
    lateinit var viewModel:LoginRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_register,container,false)
        viewModel = (activity as LoginAndRegisterActivity).loginRegisterViewModel
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLinkForText()
        setOnclickRegisterBtn()
    }
    fun setLinkForText(){
        val linkRegister = Link("Login")
            .setTextColor(Color.BLACK)
            .setBold(true)
            .setUnderlined(false)
            .setOnClickListener {
                val gotoFragmentRegister = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(gotoFragmentRegister)
            }
        binding.loginTestView.applyLinks(linkRegister)
    }
    fun setOnclickRegisterBtn(){
        binding.registerBtn.setOnClickListener {
            if (checkEmptyInputField()){
                CoroutineScope(Dispatchers.Default).launch {
                    val dialog = LoadingDialog()
                    dialog.show(requireActivity().supportFragmentManager,"loading dialog")
                    dialog.isCancelable = false
                    val result = viewModel.registerAction(RegisterRequest(
                        binding.emailRegisterTextEdit.text.toString(),
                        binding.passwordRegisterEditText.text.toString(),
                        binding.userRegisterEditText.text.toString(),
                        binding.phoneRegisterEditText.text.toString())).await()
                    dialog.cancelDialog()
                    Snackbar.make(binding.root,result,Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    //validation
    fun checkEmptyInputField():Boolean{
        var flag = true
        if(binding.emailRegisterTextEdit.text.toString().trim().isEmpty()) {
            binding.EmailInputLayout.error = "Email is empty"
            flag = false
        }
        else{
            binding.EmailInputLayout.error = ""
            flag = true
        }
        if(binding.passwordRegisterEditText.text.toString().trim().isEmpty()){
            binding.PasswordInputLayout.error = "password is empty"
            flag = false
        }
        else{
            binding.PasswordInputLayout.error = ""
            flag = true
        }
        if(binding.userRegisterEditText.text.toString().trim().isEmpty()){
            binding.UserInputLayout.error = "password is empty"
            flag = false
        }
        else{
            binding.UserInputLayout.error = ""
            flag = true
        }
        if(binding.phoneRegisterEditText.text.toString().trim().isEmpty()){
            binding.PhoneInputLayout.error = "password is empty"
            flag = false
        }
        else{
            binding.PhoneInputLayout.error = ""
            flag = true
        }
        return flag
    }
}