package com.example.quanlychitieu.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.quanlychitieu.R
import com.example.quanlychitieu.api.CreateBudgetRequest
import com.example.quanlychitieu.databinding.DialogAddBudgetBinding
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.*

class AddBudgetDialog (val idWallet:String): DialogFragment() {
    lateinit var binding:DialogAddBudgetBinding
    lateinit var viewModel:HomeViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val builder=AlertDialog.Builder(it)
            val inflater=requireActivity().layoutInflater
            binding=DataBindingUtil.inflate(inflater, R.layout.dialog_add_budget,null,false)
            viewModel=(activity as HomeActivity).homeViewModel
            setupButton()
            builder.setView(binding.root)
            builder.create()
        }?:throw IllegalStateException("Activity must not empty")
    }

    fun setupButton(){
        var c=Calendar.getInstance()
        val year=c.get(Calendar.YEAR)
        val month=c.get(Calendar.MONTH)
        val day=c.get(Calendar.DATE)

        binding.btnPickDate.setOnClickListener{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val datePickerDialog= DatePickerDialog(requireContext(),
                    DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    binding.tvDate.setText(""+year+"/"+(month+1)+"/"+day)
                    c.set(year,month,day)
                },year,month,day)
                datePickerDialog.show()
            }
        }
        binding.btnSaveBudget.setOnClickListener {
            var amount=binding.edtAddAmount.text.toString()
            var note=binding.edtAddNote.text.toString()
            if(checkField(amount,c)){
                val sharedPreferences=requireActivity().getSharedPreferences("com.example.quanlychitieu",
                    Context.MODE_PRIVATE)
                val token=sharedPreferences.getString("accountToken",null)
                var budgetRequest=CreateBudgetRequest(idWallet.toInt(),amount.toDouble(),note,java.sql.Date(c.timeInMillis))
                CoroutineScope(Dispatchers.Default).async {
                    val result=viewModel.createBudget(token!!,budgetRequest).await()
                    if(result[0]=="200"){
                        viewModel.getAllBudget(token,idWallet)
                        dialog?.cancel()
                    }
                    else{
                        Snackbar.make(binding.root,result[1], Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    fun checkField(amount:String,c: Calendar):Boolean{
        if(amount.isEmpty() || amount==""){
            Toast.makeText(context,"Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(amount.toDouble()<1000 || amount.toDouble() >100000000000){
            Toast.makeText(context,"Số tiền không hợp lệ", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(java.sql.Date(c.timeInMillis) < java.sql.Date(System.currentTimeMillis()) ){
            Toast.makeText(context,"Không được chọn ngày ở quá khứ", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}