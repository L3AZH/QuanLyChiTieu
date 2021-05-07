package com.example.quanlychitieu.dialog

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
import com.example.quanlychitieu.api.UpdateTransaction
import com.example.quanlychitieu.databinding.DialogSuaGiaoDichBinding
import com.example.quanlychitieu.db.modeldb.TransType
import com.example.quanlychitieu.db.modeldb.WalletType
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.*

class EditTransDialog (val listTransType: List<TransType>?, val listWalletType:List<WalletType>?,
                       var transInfoResponse: TransInfoResponse, val idWallet:String) : DialogFragment(){
    lateinit var binding:DialogSuaGiaoDichBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater=requireActivity().layoutInflater
        binding=DataBindingUtil.inflate(inflater, R.layout.dialog_sua_giao_dich,null,false)
        viewModel=(requireActivity() as HomeActivity).homeViewModel
        loadDetail()
        setButtonEdit()
        return super.onCreateDialog(savedInstanceState)
    }

    //idTransType, note với amount
    fun loadDetail(){
        binding.edtEditID.setText(transInfoResponse.idTransaction.toString(), TextView.BufferType.EDITABLE)
        binding.edtEditID.isEnabled=false

        val walletTypeDropDownAdapter = WalletTypeDropDownAdapter(requireContext(), listWalletType!!)
        binding.walletTypeSpinner.adapter = walletTypeDropDownAdapter
        binding.walletTypeSpinner.setSelection(transInfoResponse.wallet_idWallet)
        binding.walletTypeSpinner.isEnabled=false

        val transTypeAdapter = TransTypeAdapter(requireContext(),listTransType!!)
        binding.transTypeSpinner.adapter=transTypeAdapter
        binding.transTypeSpinner.setSelection(transInfoResponse.transType_id)
        binding.edtEditAmount.setText(transInfoResponse.amount.toString(),TextView.BufferType.EDITABLE)
        val dateSys= Calendar.getInstance().time
        binding.edtEditDate.updateDate(dateSys.year,dateSys.month,dateSys.day)
        binding.edtEditDate.isEnabled=false
        binding.edtEditNote.setText(transInfoResponse.note)
    }

    fun setButtonEdit(){
        binding.btnEdtTrans.setOnClickListener{
            val sharedPreferences=requireActivity().getSharedPreferences("com.example.quanlychitieu",Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token",null)
            CoroutineScope(Dispatchers.Default).launch{
                //Check field
                var id=binding.edtEditID.text.toString().toInt()
                var transTypeSelected=binding.transTypeSpinner.selectedItem as TransType
                var amount=binding.edtEditAmount.text.toString()
                var note=binding.edtEditNote.text.toString()
                if(amount==null || amount==""){
                    Toast.makeText(context,"Không được bỏ trống số tiền",Toast.LENGTH_SHORT).show()
                }
                else{
                    var updateTransaction=UpdateTransaction(id,transTypeSelected.idTransType,amount.toDouble(),note)
                    val result=viewModel.editTransaction(token!!,updateTransaction).await()
                    if(result[0].equals("200")){
                        viewModel.getAllTransaction(token,idWallet)
                    }
                    else{
                        Toast.makeText(context,result[1],Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.btnEdtCancel.setOnClickListener{
            dialog?.cancel()
        }
    }

}