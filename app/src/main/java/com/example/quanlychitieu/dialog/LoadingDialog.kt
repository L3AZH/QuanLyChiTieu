package com.example.quanlychitieu.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.quanlychitieu.R
import com.example.quanlychitieu.databinding.LoadingLoginRegisterScreenBinding
import java.lang.IllegalStateException

class LoadingDialog:DialogFragment() {

    lateinit var binding:LoadingLoginRegisterScreenBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            binding = DataBindingUtil.inflate(inflater, R.layout.loading_login_register_screen,null,false)
            builder.setView(binding.root)
            builder.create()
        }?:throw IllegalStateException("Activity can not be null")
    }

    fun cancelDialog(){
        dialog!!.cancel()
    }
}