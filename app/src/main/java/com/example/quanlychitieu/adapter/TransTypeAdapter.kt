package com.example.quanlychitieu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.example.quanlychitieu.R
import com.example.quanlychitieu.db.modeldb.TransType

class TransTypeAdapter(val context: Context, listTransType:List<TransType>): BaseAdapter() {
    override fun getCount(): Int {
        return listTransType.size
    }

    override fun getItem(position: Int): Any {
        return listTransType[position]
    }

    override fun getItemId(position: Int): Long {
        return listTransType[position].idTransType
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val dataBinding:TransTypeAdapterBinding=
            DataBindingUtil.inflate(LayoutInflater.from(parent!!.context),
                R.layout.item_transtype,null,false)
        dataBinding.itemIdTrans.text=listTransType[position].idTransType
        dataBinding.itemTypeTrans.text=listTransType[position].type
    }
}