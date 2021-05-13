package com.example.quanlychitieu.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.quanlychitieu.R
import com.example.quanlychitieu.api.BudgetInfoResponse
import com.example.quanlychitieu.api.UpdateBudgetRequest
import com.example.quanlychitieu.api.UpdateTransactionRequest
import com.example.quanlychitieu.databinding.DialogSuaBudgetBinding
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class EditBudgetDialog(val budgetInfoResponse: BudgetInfoResponse) :DialogFragment() {
    lateinit var binding:DialogSuaBudgetBinding
    lateinit var viewModel:HomeViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            var builder=AlertDialog.Builder(it)
            val inflater=requireActivity().layoutInflater
            binding=DataBindingUtil.inflate(inflater, R.layout.dialog_sua_budget,null,false)
            viewModel=(activity as HomeActivity).homeViewModel
            loadDetail()
            setButton()
            builder.setView(binding.root)
            builder.create()
        }?:throw IllegalStateException("Activity must not empty")
    }

    fun loadDetail(){
        binding.edtEditIDBudget.setText(budgetInfoResponse.idBudget.toString())
        binding.edtEditIDBudget.isEnabled=false

        binding.edtIdWallet.setText(budgetInfoResponse.walletIdWallet.toString())
        binding.edtIdWallet.isEnabled=false

        binding.edtEditAmount.setText(budgetInfoResponse.amount.toString())
        binding.tvDate.setText(budgetInfoResponse.date.toString())
        binding.edtEditNote.setText(budgetInfoResponse.note)
    }

    fun setButton(){
        var c=Calendar.getInstance()
        val year=budgetInfoResponse.date.date
        val month=budgetInfoResponse.date.month
        val day=budgetInfoResponse.date.year
        c.set(budgetInfoResponse.date.year,budgetInfoResponse.date.month,budgetInfoResponse.date.date)

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
        binding.btnEdtTrans.setOnClickListener {
            val sharedPreferences=requireActivity().getSharedPreferences("com.example.quanlychitieu",Context.MODE_PRIVATE)
            val token=sharedPreferences.getString("accountToken",null)
            CoroutineScope(Dispatchers.Default).async {
                //CheckField
                var amount=binding.edtEditAmount.text.toString()
                var note=binding.edtEditNote.text.toString().trim()
                if(checkField(amount,c)){
                    var updateBudget= UpdateBudgetRequest(amount.toDouble(),note,java.sql.Date(c.timeInMillis))
                    val result=viewModel.updateBudget(token!!,budgetInfoResponse.idBudget,updateBudget).await()
                    if(result[0].equals("200")){
                        viewModel.getAllBudget(token,budgetInfoResponse.walletIdWallet.toString())
                        dialog?.cancel()
                    }
                    else{
                        Snackbar.make(binding.root,result[1], Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
        binding.btnDeleteTrans.setOnClickListener {
            val sharedPreferences=requireActivity().getSharedPreferences("com.example.quanlychitieu",Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("accountToken",null)
            CoroutineScope(Dispatchers.Default).launch{
                //Check field
                var id=budgetInfoResponse.idBudget
                var idWallet=budgetInfoResponse.walletIdWallet.toString()
                val result=viewModel.deleteBudget(token!!,id).await()
                if(result[0].equals("200")){
                    viewModel.getAllBudget(token,idWallet)
                    dialog?.cancel()
                }
                else{
                    Snackbar.make(binding.root,result[1], Snackbar.LENGTH_LONG).show()
                }
            }
        }
        binding.btnEdtCancel.setOnClickListener {
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