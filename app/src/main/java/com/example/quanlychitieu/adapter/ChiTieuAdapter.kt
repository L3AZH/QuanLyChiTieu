package com.example.quanlychitieu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
        holder.setUpItemClick(trans,onItemClickListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ChiTieuViewHolder(val binding:ChiTieuRecyclerviewBinding): RecyclerView.ViewHolder(binding.root){
        fun setUpItemClick(transInfo:TransInfoResponse, listener: ((TransInfoResponse) -> Unit)?){
            println("ID GD: "+transInfo.idTransaction.toString())
            binding.tvIdTransaction.text= "ID giao d???ch: "+transInfo.idTransaction.toString()
            binding.tvIdWallet.text="ID v??: "+transInfo.wallet_idWallet
            binding.tvIdTransType.text="Lo???i giao d???ch: "+transInfo.type
            binding.tvAmount.text="S??? ti???n: "+transInfo.amount
            binding.tvDate.text="Ng??y: "+transInfo.date
            binding.tvNote.text="Ghi ch??: "+transInfo.note
            itemView.setOnClickListener {
                listener?.let {
                    it(transInfo)
                }
            }
        }
    }
}