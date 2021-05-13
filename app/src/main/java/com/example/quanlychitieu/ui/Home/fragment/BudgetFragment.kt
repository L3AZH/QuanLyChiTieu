package com.example.quanlychitieu.ui.Home.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.BudgetAdapter
import com.example.quanlychitieu.api.BudgetInfoResponse
import com.example.quanlychitieu.databinding.FragmentBudgetBinding
import com.example.quanlychitieu.databinding.ItemBudgetRecyclerviewBinding
import com.example.quanlychitieu.dialog.AddBudgetDialog
import com.example.quanlychitieu.dialog.EditBudgetDialog
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel

class BudgetFragment(idWalletIn:String) : Fragment() {
    lateinit var binding:FragmentBudgetBinding
    lateinit var viewModel:HomeViewModel
    lateinit var adapter:BudgetAdapter
    val idWallet=idWalletIn

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_budget,container,false)
        viewModel=(activity as HomeActivity).homeViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setButtonAdd()
        adapter= BudgetAdapter()
        binding.rvBudget.layoutManager= LinearLayoutManager(activity)
        binding.rvBudget.adapter=adapter
        adapter.setOnItemClickListener {
            setOnItemClick(it)
        }
        viewModel.listBudget.observe(viewLifecycleOwner, Observer{ response->
            adapter.differ.submitList(response)
        })

        val sharedPreference =
            requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharedPreference.getString("accountToken", "null")
        viewModel.getAllBudget(token!!,idWallet)
        super.onViewCreated(view, savedInstanceState)
    }

    fun setOnItemClick(budgetInfoResponse: BudgetInfoResponse){
        val dialog=EditBudgetDialog(budgetInfoResponse)
        dialog.show(requireActivity().supportFragmentManager,"Edit budget")
        dialog.isCancelable=false
    }

    fun setButtonAdd(){
        binding.btnAddBudget.setOnClickListener {
            val dialog=AddBudgetDialog(idWallet)
            dialog.show(requireActivity().supportFragmentManager,"Add budget")
            dialog.isCancelable=false
        }
    }
}