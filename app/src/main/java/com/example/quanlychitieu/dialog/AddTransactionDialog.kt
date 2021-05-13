package com.example.quanlychitieu.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.TransTypeAdapter
import com.example.quanlychitieu.adapter.WalletTypeDropDownAdapter
import com.example.quanlychitieu.api.CreateTransactionRequest
import com.example.quanlychitieu.databinding.DialogAddTransactionBinding
import com.example.quanlychitieu.db.modeldb.TransType
import com.example.quanlychitieu.db.modeldb.WalletType
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.sql.Date
import java.util.*

class AddTransactionDialog (val listTransType:List<TransType>, val idWallet:String, var walletAmount:String) :DialogFragment(){
    lateinit var binding: DialogAddTransactionBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            binding =
                DataBindingUtil.inflate(inflater, R.layout.dialog_add_transaction, null, false)
            viewModel = (requireActivity() as HomeActivity).homeViewModel
            loadDetail()
            setUpButton()
            builder.setView(binding.root)
            builder.create()
        }?: throw IllegalStateException("Activity must not empty")
    }

    fun loadDetail(){
        binding.edtIdWallet.setText(idWallet)
        binding.edtIdWallet.isEnabled=false
        binding.tvDate.isEnabled=false
        val transTypeAdapter=TransTypeAdapter(requireContext(),listTransType)
        binding.transTypeSpinner.adapter=transTypeAdapter
    }

    fun setUpButton(){
        var c= Calendar.getInstance()
        val year=c.get(Calendar.YEAR)
        val month=c.get(Calendar.MONTH)
        val day=c.get(Calendar.DATE)

        binding.btnPickDate.setOnClickListener{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val datePickerDialog= DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    binding.tvDate.setText(""+year+"/"+(month+1)+"/"+day)
                    c.set(year,month,day)
                },year,month,day)
                datePickerDialog.show()
            }
        }
        binding.btnSaveTrans.setOnClickListener {
            var transType=binding.transTypeSpinner.selectedItem as TransType
            var amount=binding.edtEditAmount.text.toString()
            var note=binding.edtEditNote.text.toString().trim()

            if(checkField(amount,c)){
                val sharedPreferences=requireActivity().getSharedPreferences("com.example.quanlychitieu",
                    Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("accountToken",null)
                println(token)
                var transRequest=CreateTransactionRequest(idWallet.toInt(),transType.idTransType,
                                                        amount.toDouble(),note, java.sql.Date(c.timeInMillis))
                println(transRequest)
                CoroutineScope(Dispatchers.Default).launch {
                    val result=viewModel.createTransaction(token!!,transRequest).await()
                    println(result)
                    if(result[0].equals("200")){
                        viewModel.getAllTransaction(token,idWallet)
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
            Toast.makeText(context,"Vui lòng nhập số tiền",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(amount.toDouble()<1000 || amount.toDouble() >100000000000){
            Toast.makeText(context,"Số tiền không hợp lệ",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(java.sql.Date(c.timeInMillis) > java.sql.Date(System.currentTimeMillis()) ){
            Toast.makeText(context,"Không được chọn ngày ở tương lai",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}