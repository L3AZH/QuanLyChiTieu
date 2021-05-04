package com.example.quanlychitieu.ui.Home.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlychitieu.MainActivity
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.ChiTieuAdapter
import com.example.quanlychitieu.databinding.FragmentChiTieuBinding
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel

class ChiTieuFragment : Fragment() {
    lateinit var binding: FragmentChiTieuBinding
    lateinit var chiTieuAdapter: ChiTieuAdapter
    lateinit var viewModel:HomeViewModel
    val args: ChiTieuFragmentArgs by navArgs()

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
        chiTieuAdapter= ChiTieuAdapter()
        binding.rvTransaction.layoutManager=LinearLayoutManager(activity)
        binding.rvTransaction.adapter=chiTieuAdapter
        viewModel.allTrans.observe(viewLifecycleOwner, Observer { response->
            chiTieuAdapter.differ.submitList(response)
        })
        val sharePreference =
            requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharePreference.getString("accountToken", "null")
        viewModel.getAllTransaction(token!!,args.idWallet)
        super.onViewCreated(view, savedInstanceState)
    }
}