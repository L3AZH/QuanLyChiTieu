package com.example.quanlychitieu.ui.Home.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.ViewPagerAdapter
import com.example.quanlychitieu.databinding.FragmentMiddleBinding
import com.google.android.material.tabs.TabLayoutMediator

class MiddleFragment : Fragment() {
    lateinit var binding:FragmentMiddleBinding
    val args:MiddleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        println("OK")
        val middleFragment: Fragment ?= requireActivity().supportFragmentManager.findFragmentById(R.id.middleFragment)
        val fragment: Fragment ?= middleFragment?.childFragmentManager?.findFragmentById(R.id.chiTieuFragment)
        val fragment2: Fragment ?= middleFragment?.childFragmentManager?.findFragmentById(R.id.budgetFragment2)
        println(fragment)
        println(fragment2)
        if (fragment != null) middleFragment?.childFragmentManager?.beginTransaction().remove(fragment)
            .commit()
        if (fragment2 != null) middleFragment?.childFragmentManager?.beginTransaction().remove(fragment2)
            .commit()

        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_middle,container,false)
        val adapter=ViewPagerAdapter(parentFragmentManager,lifecycle,args.idWallet)
        binding.viewPager.adapter=adapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,position->
            tab.text = "Tab ${position+1}"

        }.attach()
        return binding.root
    }

}