package com.example.quanlychitieu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.R
import com.example.quanlychitieu.api.WalletInfo
import com.example.quanlychitieu.databinding.ItemWalletRecycleviewBinding

class ViAdapter():RecyclerView.Adapter<ViAdapter.ViViewHolder>() {

    private var onItemClickListener:((WalletInfo)->Unit)?=null
    fun setOnItemClickListener(listener: ((WalletInfo) -> Unit)){
        this.onItemClickListener = listener
    }

    var differCallBack = object :DiffUtil.ItemCallback<WalletInfo>(){
        override fun areItemsTheSame(oldItem: WalletInfo, newItem: WalletInfo): Boolean {
            return oldItem.idWallet == newItem.idWallet
        }

        override fun areContentsTheSame(oldItem: WalletInfo, newItem: WalletInfo): Boolean {
            return oldItem == newItem
        }
    }

    var diff = AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:ItemWalletRecycleviewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_wallet_recycleview,parent,false)
        return ViViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViViewHolder, position: Int) {
        holder.setUpBinding(diff.currentList[position],onItemClickListener!!)
    }

    override fun getItemCount(): Int {
        if(diff.currentList == null) return 0
        return diff.currentList.size
    }

    inner class ViViewHolder(val binding:ItemWalletRecycleviewBinding):RecyclerView.ViewHolder(binding.root){
        fun setUpBinding(walletInfo:WalletInfo,listener:((WalletInfo)->Unit)?){
            binding.idWalletTextView.text = "Id Wallet: "+walletInfo.idWallet
            binding.accountEmailTextView.text = "Account Email: "+walletInfo.accountEmail
            binding.amountWalletTextView.text = "Amount: "+walletInfo.amount
            binding.typeWalletTextView.text = "Type: "+walletInfo.type
            itemView.setOnClickListener {
                listener?.let {
                    it(walletInfo)
                }
            }
        }
    }
}