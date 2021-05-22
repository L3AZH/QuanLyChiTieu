package com.example.quanlychitieu.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.quanlychitieu.ui.Home.fragment.BudgetFragment
import com.example.quanlychitieu.ui.Home.fragment.ChiTieuFragment


class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    idWallet: String,
    walletAmount: String
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    val num_items = 2
    val idWallet = idWallet
    var walletAmount = walletAmount

    override fun getItemCount(): Int {
        return num_items
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> {
                BudgetFragment(idWallet)
            }
            else -> {
                ChiTieuFragment(idWallet, walletAmount)

            }
        }
    }
}
