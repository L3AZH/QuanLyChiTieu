package com.example.quanlychitieu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.R
import com.example.quanlychitieu.api.BudgetInfoResponse
import com.example.quanlychitieu.databinding.ItemBudgetRecyclerviewBinding

class BudgetAdapter(): RecyclerView.Adapter<BudgetAdapter.BudgetAdapterViewHolder> (){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BudgetAdapter.BudgetAdapterViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding:ItemBudgetRecyclerviewBinding = DataBindingUtil.inflate(inflater, R.layout.item_budget_recyclerview,parent,false)
        return BudgetAdapterViewHolder(binding)
    }

    private val differCallback=object: DiffUtil.ItemCallback<BudgetInfoResponse>(){
        override fun areItemsTheSame(
            oldItem: BudgetInfoResponse,
            newItem: BudgetInfoResponse
        ): Boolean {
            return oldItem.idBudget==newItem.idBudget
        }

        override fun areContentsTheSame(
            oldItem: BudgetInfoResponse,
            newItem: BudgetInfoResponse
        ): Boolean {
            return oldItem==newItem
        }
    }
    val differ= AsyncListDiffer(this,differCallback)

    private var onItemClickListener:((BudgetInfoResponse) -> Unit)?=null
    fun setOnItemClickListener(listener:(BudgetInfoResponse) ->Unit){
        onItemClickListener=listener
    }

    override fun onBindViewHolder(holder: BudgetAdapter.BudgetAdapterViewHolder, position: Int) {
        val current=differ.currentList[position]
        holder.setUpItemClick(current,onItemClickListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class BudgetAdapterViewHolder(val binding:ItemBudgetRecyclerviewBinding): RecyclerView.ViewHolder(binding.root){
        fun setUpItemClick(budgetInfoResponse:BudgetInfoResponse, listener: ((BudgetInfoResponse) -> Unit)?){
            binding.tvIdBudget.text="ID: "+budgetInfoResponse.idBudget.toString()
            binding.tvAmount.text="Amount: "+budgetInfoResponse.amount.toString()
            binding.tvNote.text="Note: "+budgetInfoResponse.note.toString()
            binding.tvDate.text="Date: "+budgetInfoResponse.date.toString()
            binding.tvIDWallet.text="ID v??: "+budgetInfoResponse.walletIdWallet.toString()
            itemView.setOnClickListener{
                listener?.let {
                    it(budgetInfoResponse)
                }
            }
        }
    }
}