package com.example.quanlychitieu.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.TransTypeAdapter
import com.example.quanlychitieu.adapter.WalletTypeDropDownAdapter
import com.example.quanlychitieu.api.RegisterRequest
import com.example.quanlychitieu.api.TransInfoResponse
import com.example.quanlychitieu.api.UpdateTransactionRequest
import com.example.quanlychitieu.databinding.DialogSuaGiaoDichBinding
import com.example.quanlychitieu.db.modeldb.TransType
import com.example.quanlychitieu.db.modeldb.WalletType
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.lang.IllegalStateException
import java.util.*

class EditTransDialog (val listTransType: List<TransType>?, val listWalletType:List<WalletType>?,
                       var transInfoResponse: TransInfoResponse, val idWallet:String) : DialogFragment(){
    lateinit var binding:DialogSuaGiaoDichBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            binding=DataBindingUtil.inflate(inflater, R.layout.dialog_sua_giao_dich,null,false)
            viewModel=(requireActivity() as HomeActivity).homeViewModel
            loadDetail()
            setButtonEdit()
            builder.setView(binding.root)
            builder.create()
        }?: throw IllegalStateException("Activity must not empty")
    }

    //idTransType, note với amount
    fun loadDetail(){
        binding.edtEditID.setText(transInfoResponse.idTransaction.toString())
        binding.edtEditID.isEnabled=false

        binding.edtIdWallet.setText(idWallet)
        binding.edtIdWallet.isEnabled=false

        val transTypeAdapter = TransTypeAdapter(requireContext(),listTransType!!)
        binding.transTypeSpinner.adapter=transTypeAdapter
        val position=transTypeAdapter.getPositionFromID(transInfoResponse.transType_id)
        binding.transTypeSpinner.setSelection(position)

        binding.edtEditAmount.setText(transInfoResponse.amount.toString(),TextView.BufferType.EDITABLE)

        binding.edtDate.setText(transInfoResponse.date.toString())
        binding.edtDate.isEnabled=false

        binding.edtEditNote.setText(transInfoResponse.note)
    }

    fun setButtonEdit(){
        binding.btnEdtTrans.setOnClickListener{
            val sharedPreferences=requireActivity().getSharedPreferences("com.example.quanlychitieu",Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("accountToken",null)
            CoroutineScope(Dispatchers.Default).launch{
                //Check field
                var id=binding.edtEditID.text.toString().toInt()
                var transTypeSelected=binding.transTypeSpinner.selectedItem as TransType
                var amount=binding.edtEditAmount.text.toString()
                var note=binding.edtEditNote.text.toString().trim()
                if(checkField(amount)){
                    var updateTransaction=UpdateTransactionRequest(transTypeSelected.idTransType,amount.toDouble(),note)
                    val result=viewModel.editTransaction(token!!,id,updateTransaction).await()
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
        binding.btnDeleteTrans.setOnClickListener {
            val sharedPreferences=requireActivity().getSharedPreferences("com.example.quanlychitieu",Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("accountToken",null)
            CoroutineScope(Dispatchers.Default).launch{
                //Check field
                var id=binding.edtEditID.text.toString().toInt()
                val result=viewModel.deleteTransaction(token!!,id).await()
                if(result[0].equals("200")){
                    viewModel.getAllTransaction(token,idWallet)
                    dialog?.cancel()
                }
                else{
                    Snackbar.make(binding.root,result[1], Snackbar.LENGTH_LONG).show()
                }
            }
        }
        binding.btnEdtCancel.setOnClickListener{
            dialog?.cancel()
        }
    }

    fun checkField(amount:String):Boolean{
        if(amount.isEmpty() || amount==""){
            Toast.makeText(context,"Vui lòng nhập số tiền",Toast.LENGTH_SHORT).show()
            return false
        }
        else if(amount.toDouble()<1000 || amount.toDouble() >100000000000){
            Toast.makeText(context,"Số tiền không hợp lệ",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}