package com.example.quanlychitieu.ui.Home.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.ViAdapter
import com.example.quanlychitieu.api.WalletInfo
import com.example.quanlychitieu.databinding.FragmentViBinding
import com.example.quanlychitieu.dialog.AddingWalletDialog
import com.example.quanlychitieu.dialog.LoadingDialog
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViFragment : Fragment() {

    lateinit var binding:FragmentViBinding
    lateinit var viewModel:HomeViewModel
    lateinit var viAdapter:ViAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_vi,container,false)
        viewModel = (activity as HomeActivity).homeViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycleView()
        setOnclickAddWalletFloatingBtn()
    }

    fun setUpRecycleView(){
        viAdapter = ViAdapter()
        viAdapter.setOnItemClickListener {
            setOnItemClick(it)
        }
        binding.listWalletRecycleView.layoutManager = LinearLayoutManager(activity)
        binding.listWalletRecycleView.adapter = viAdapter
        viewModel.listWallet.observe(viewLifecycleOwner, Observer { listWallet ->
            viAdapter.diff.submitList(listWallet)
        })
        val sharePreference =
            requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharePreference.getString("accountToken", "null")
        viewModel.setListWallet(token!!)
    }
    fun setOnItemClick(walletInfo: WalletInfo){
        Toast.makeText(context, "itemclick"+walletInfo.idWallet, Toast.LENGTH_SHORT).show()
    }
    fun setOnclickAddWalletFloatingBtn(){
        CoroutineScope(Dispatchers.Default).launch {
            val listWalletType = viewModel.getListWalletFromDb().await()
            binding.addWalletFloatingActionButton.setOnClickListener {
                if(listWalletType == null){
                    Snackbar.make(binding.root,"List Wallet Type is empty !!",Snackbar.LENGTH_LONG).show()
                }
                else{
                    val addDialog = AddingWalletDialog(listWalletType)
                    addDialog.show(requireActivity().supportFragmentManager,"adding wallet type dialog")
                    addDialog.isCancelable = false
                }
            }
        }
    }
}