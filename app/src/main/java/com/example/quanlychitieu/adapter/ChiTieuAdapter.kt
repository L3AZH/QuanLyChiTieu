package com.example.quanlychitieu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.R
import com.example.quanlychitieu.api.TransInfoResponse
import com.example.quanlychitieu.databinding.ChiTieuRecyclerviewBinding
import com.example.quanlychitieu.db.modeldb.Transaction

class ChiTieuAdapter(): RecyclerView.Adapter<ChiTieuAdapter.ChiTieuViewHolder>(){
    private var differCallback=object :DiffUtil.ItemCallback<Transaction>(){
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.idTransaction==newItem.idTransaction
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChiTieuAdapter.ChiTieuViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val dataBinding:ChiTieuRecyclerviewBinding=
            DataBindingUtil.inflate(inflater,R.layout.chi_tieu_recyclerview,parent,false)
        return ChiTieuViewHolder(dataBinding)
    }

    val differ=AsyncListDiffer(this,differCallback)

    override fun onBindViewHolder(holder: ChiTieuAdapter.ChiTieuViewHolder, position: Int) {
        val trans=differ.currentList[position]

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ChiTieuViewHolder(val binding:ChiTieuRecyclerviewBinding): RecyclerView.ViewHolder(binding.root){
        fun setUp(transInfo:TransInfoResponse,listener: ((TransInfoResponse) ->Unit )?){
            binding.tvIdTransaction.text= "ID giao dịch: "+transInfo.idTransaction.toString()
            binding.tvIdWallet.text="ID ví: "+transInfo.wallet_idWallet
            binding.tvIdTransType.text="Loại giao dịch: "+transInfo.type
            binding.tvAmount.text="Số tiền: "+transInfo.amount
            binding.tvDate.text="Ngày: "+transInfo.date
            binding.tvNote.text="Ghi chú: "+transInfo.note
            itemView.setOnClickListener{
                listener?.let {
                    it(transInfo)
                }
            }
        }
    }
}