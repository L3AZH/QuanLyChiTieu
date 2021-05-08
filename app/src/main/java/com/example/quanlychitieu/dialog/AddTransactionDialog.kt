package com.example.quanlychitieu.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.sql.Date
import java.time.Instant

class AddTransactionDialog (val listTransType:List<TransType>, val listWalletType:List<WalletType>, val idWallet:String) :DialogFragment(){
    lateinit var binding: DialogAddTransactionBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater=requireActivity().layoutInflater
        binding=DataBindingUtil.inflate(inflater,R.layout.dialog_add_transaction,null,false)
        viewModel=(requireActivity() as HomeActivity).homeViewModel
        loadDetail()
        setUpButton()
        return super.onCreateDialog(savedInstanceState)
    }

    fun loadDetail(){
        val walletAdapter=WalletTypeDropDownAdapter(requireContext(),listWalletType)
        binding.walletTypeSpinner.adapter=walletAdapter

        val transTypeAdapter=TransTypeAdapter(requireContext(),listTransType)
        binding.transTypeSpinner.adapter=transTypeAdapter

    }

    fun setUpButton(){
        binding.btnSaveTrans.setOnClickListener {
            var walletType=binding.walletTypeSpinner.selectedItem as WalletType
            var transType=binding.transTypeSpinner.selectedItem as TransType
            var amount=binding.edtEditAmount.text.toString()
            var note=binding.edtEditNote.text.toString().trim()
            var dateToSave=Date(binding.edtEditDate.year,
                                            binding.edtEditDate.month,
                                            binding.edtEditDate.dayOfMonth)
            if(checkField(amount)){
                val sharedPreferences=requireActivity().getSharedPreferences("com.example.quanlychitieu",
                    Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("token",null)
                var transRequest=CreateTransactionRequest(walletType.idWalletType,transType.idTransType,
                                                        amount.toDouble(),note,dateToSave)
                CoroutineScope(Dispatchers.Default).async {
                    val result=viewModel.createTransaction(token!!,transRequest).await()
                    if(result[0].equals("200")){
                        viewModel.getAllTransaction(token,idWallet)
                        dialog?.cancel()
                    }
                    else{
                        Toast.makeText(context,result[1],Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.btnEdtCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    fun checkField(amount:String):Boolean{
        if(amount.isEmpty() || amount==""){
            Toast.makeText(context,"Vui lòng nhập số tiền",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(amount.toDouble()<1000){
            Toast.makeText(context,"Số tiền không hợp lệ",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}