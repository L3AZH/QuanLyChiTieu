package com.example.quanlychitieu.ui.Home.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.ViAdapter
import com.example.quanlychitieu.api.WalletInfo
import com.example.quanlychitieu.databinding.FragmentViBinding
import com.example.quanlychitieu.dialog.AddingWalletDialog
import com.example.quanlychitieu.dialog.LoadingDialog
import com.example.quanlychitieu.dialog.UpdateWalletDialog
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
        //setOnclickAddWalletFloatingBtn()

    }

    fun setUpRecycleView(){
        viAdapter = ViAdapter()
        viAdapter.setOnItemClickListener {
            setOnItemClick(it)
        }
        viAdapter.setOnItemEditBtnClickListener {
            setOnEditItemClick(it)
        }
        viAdapter.setOnItemDeleteBtnClickListener {
            setOnDeleteItemClick(it)
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
        Toast.makeText(context, "itemclick "+walletInfo.idWallet, Toast.LENGTH_SHORT).show()
        val bundle = Bundle().apply {
            putSerializable("idWallet",walletInfo.idWallet)
            putSerializable("walletAmount",walletInfo.amount)
        }
        println("ID Ví fragment: "+walletInfo.idWallet)
        findNavController().navigate(
            R.id.action_viFragment_to_middleFragment,
            bundle
        )
    }
    fun setOnDeleteItemClick(walletInfo: WalletInfo){
        val sharePreference =
            requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharePreference.getString("accountToken", "null")
        CoroutineScope(Dispatchers.Default).launch {
            val loadingDialog = LoadingDialog()
            loadingDialog.show(requireActivity().supportFragmentManager,"loading dialog")
            val result = viewModel.deleteWallet(token!!,walletInfo.type).await()
            loadingDialog.cancelDialog()
            Snackbar.make(binding.root,result[1],Snackbar.LENGTH_LONG).show()
            viewModel.setListWallet(token!!)
        }
    }
    fun setOnEditItemClick(walletInfo: WalletInfo){
        val updateDialog = UpdateWalletDialog(walletInfo)
        updateDialog.show(requireActivity().supportFragmentManager,"Update Wallet Dialog")
        updateDialog.isCancelable = false
    }
    /*fun setOnclickAddWalletFloatingBtn(){
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
    }*/
}