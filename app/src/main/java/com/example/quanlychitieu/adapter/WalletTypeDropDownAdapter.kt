package com.example.quanlychitieu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import com.example.quanlychitieu.R
import com.example.quanlychitieu.databinding.OptionItemWallettypeTextinputlayoutDialogAddwalletBinding
import com.example.quanlychitieu.db.modeldb.WalletType


class WalletTypeDropDownAdapter(val context: Context, val listWalletType: List<WalletType>) :
    BaseAdapter() {

    override fun getCount(): Int {
        if (listWalletType == null) return 0
        return listWalletType.size
    }

    override fun getItem(position: Int): Any? {
        if (listWalletType == null) return null
        return listWalletType[position]
    }

    override fun getItemId(position: Int): Long {
        if(listWalletType == null) return -1
        return listWalletType[position].idWalletType.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View
        var viewHolder:WalletTypeDropDownViewHolder
        if(convertView == null){
            view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.option_item_wallettype_textinputlayout_dialog_addwallet,parent,false)
            viewHolder = WalletTypeDropDownViewHolder(view)
            view.tag = viewHolder
        }
        else{
            view = convertView
            viewHolder = view.tag as WalletTypeDropDownViewHolder
        }
        viewHolder.idWalletTextView.text = listWalletType[position].idWalletType.toString()
        viewHolder.typeWalletTextView.text = listWalletType[position].type
        return view
    }

    inner class WalletTypeDropDownViewHolder(val row:View?){
        var idWalletTextView:TextView
        var typeWalletTextView:TextView
        init {
            idWalletTextView = row!!.findViewById(R.id.idWalletTypeTextView)
            typeWalletTextView = row!!.findViewById(R.id.typeWalletTextView)
        }
    }
}