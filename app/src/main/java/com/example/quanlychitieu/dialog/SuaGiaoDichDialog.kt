package com.example.quanlychitieu.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.WalletTypeDropDownAdapter
import com.example.quanlychitieu.api.TransInfoResponse
import com.example.quanlychitieu.databinding.DialogSuaGiaoDichBinding
import com.example.quanlychitieu.db.modeldb.WalletType
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel

class SuaGiaoDichDialog (val walletType:WalletType,var transInfoResponse: TransInfoResponse) : DialogFragment(){
    lateinit var binding:DialogSuaGiaoDichBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater=requireActivity().layoutInflater
        binding=DataBindingUtil.inflate(inflater, R.layout.dialog_sua_giao_dich,null,false)
        viewModel=(requireActivity() as HomeActivity).homeViewModel
        return super.onCreateDialog(savedInstanceState)
    }

    fun loadDetail(walletType: WalletType,transInfoResponse: TransInfoResponse){
//        walletTypeDropDownAdapter = WalletTypeDropDownAdapter(requireContext(), listWalletType)
//        binding.walletTypeSpinner.adapter = walletTypeDropDownAdapter
    }
}