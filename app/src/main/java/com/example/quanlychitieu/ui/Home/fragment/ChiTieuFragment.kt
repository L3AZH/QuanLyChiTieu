package com.example.quanlychitieu.ui.Home.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.ChiTieuAdapter
import com.example.quanlychitieu.api.TransInfoResponse
import com.example.quanlychitieu.databinding.FragmentChiTieuBinding
import com.example.quanlychitieu.dialog.AddTransactionDialog
import com.example.quanlychitieu.dialog.EditTransDialog
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class ChiTieuFragment(idWalletIn:String) : Fragment() {
    lateinit var binding: FragmentChiTieuBinding
    lateinit var chiTieuAdapter: ChiTieuAdapter
    lateinit var viewModel:HomeViewModel
    var idWallet:String=idWalletIn

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_chi_tieu, container, false)
        viewModel=(activity as HomeActivity).homeViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBtnAdd()
        chiTieuAdapter= ChiTieuAdapter()
        binding.rvTransaction.layoutManager=LinearLayoutManager(activity)
        binding.rvTransaction.adapter=chiTieuAdapter
        chiTieuAdapter.setOnItemClickListener {
            setOnItemClick(it)
        }

        viewModel.allTrans.observe(viewLifecycleOwner, Observer { response->
            chiTieuAdapter.differ.submitList(response)
        })
        val sharePreference =
            requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharePreference.getString("accountToken", "null")
        viewModel.getAllTransaction(token!!,idWallet)
        println("ID Chi tiêu fragment: "+idWallet)
        super.onViewCreated(view, savedInstanceState)
    }

    fun setOnItemClick(transInfoResponse: TransInfoResponse){
        CoroutineScope(Dispatchers.Default).launch{
            val transType=viewModel.getListTransTypeFromDB().await()
            val walletType=viewModel.getListWalletFromDb().await()
            val dialog=EditTransDialog(transType,walletType,transInfoResponse,idWallet)
            dialog.show(requireActivity().supportFragmentManager,"Edit transaction")
            dialog.isCancelable=false
        }
    }

    fun setBtnAdd(){
        binding.btnAddTrans.setOnClickListener {
            CoroutineScope(Dispatchers.Default).async{
                var transType=viewModel.getListTransTypeFromDB().await()
                var dialog=AddTransactionDialog(transType!!,idWallet)
                dialog.show(requireActivity().supportFragmentManager,"Add transaction")
                dialog.isCancelable = false
            }
        }
    }

}