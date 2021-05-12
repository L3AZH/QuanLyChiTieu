package com.example.quanlychitieu.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.quanlychitieu.R
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.fragment.BudgetFragment
import com.example.quanlychitieu.ui.Home.fragment.ChiTieuFragment


class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle,idWallet:String)
    : FragmentStateAdapter(fragmentManager,lifecycle)
{
    val num_items=2
    val idWallet=idWallet

    override fun getItemCount():Int{
        return num_items
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            1->{
                BudgetFragment()
            }
            else->{
                ChiTieuFragment(idWallet)
                
            }
        }
    }
}
