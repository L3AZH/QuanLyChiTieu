package com.example.quanlychitieu.adapter

import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.R
import com.example.quanlychitieu.api.TransInfoResponse
import com.example.quanlychitieu.databinding.ChiTieuRecyclerviewBinding

class ChiTieuAdapter(): RecyclerView.Adapter<ChiTieuAdapter.ChiTieuViewHolder>(){
    private var differCallback=object :DiffUtil.ItemCallback<TransInfoResponse>(){
        override fun areItemsTheSame(oldItem: TransInfoResponse, newItem: TransInfoResponse): Boolean {
            return oldItem.idTransaction==newItem.idTransaction
        }

        override fun areContentsTheSame(oldItem: TransInfoResponse, newItem: TransInfoResponse): Boolean {
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

    private var onItemClickListener:((TransInfoResponse) -> Unit)?=null
    fun setOnItemClickListener(listener:(TransInfoResponse) ->Unit){
        onItemClickListener=listener
    }

    override fun onBindViewHolder(holder: ChiTieuAdapter.ChiTieuViewHolder, position: Int) {
        val trans=differ.currentList[position]
        holder.setUp(trans,onItemClickListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ChiTieuViewHolder(val binding:ChiTieuRecyclerviewBinding): RecyclerView.ViewHolder(binding.root){
        fun setUp(transInfo:TransInfoResponse, listener: ((TransInfoResponse) -> Unit)?){
            println("ID GD: "+transInfo.idTransaction.toString())
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