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
    lateinit var binding: FragmentMiddleBinding
    val args: MiddleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_middle, container, false)
        val adapter =
            ViewPagerAdapter(parentFragmentManager, lifecycle, args.idWallet, args.walletAmount)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Giao dịch"
                }
                1 -> {
                    tab.text = "Kế hoạch"
                }
            }

        }.attach()
        return binding.root
    }

}