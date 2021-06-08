package com.example.quanlychitieu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlychitieu.R
import com.example.quanlychitieu.db.modeldb.TransType

class TransTypeAdapter(val context: Context, val listTransType: List<TransType>): BaseAdapter() {
    override fun getCount(): Int {
        return listTransType.size
    }

    override fun getItem(position: Int): Any {
        return listTransType[position]
    }

    override fun getItemId(position: Int): Long {
        return listTransType[position].idTransType.toLong()
    }

    fun getPositionFromID(id:Int):Int{
        var position:Int=-1
        for (item in listTransType){
            if (item.idTransType==id) position=listTransType.indexOf(item)
        }
        return position
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View
        var viewHolder:TransTypeViewHolder
        if(convertView == null){
            view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_transtype,parent,false)
            viewHolder = TransTypeViewHolder(view)
            view.tag = viewHolder
        }
        else{
            view = convertView
            viewHolder = view.tag as TransTypeAdapter.TransTypeViewHolder
        }
        viewHolder.itemIdTrans.text=listTransType[position].idTransType.toString()
        viewHolder.itemTypeTrans.text=listTransType[position].categoryName
        return view

    }

    inner class TransTypeViewHolder(val row:View?){
        var itemIdTrans: TextView
        var itemTypeTrans: TextView
        init {
            itemIdTrans = row!!.findViewById(R.id.itemIdTrans)
            itemTypeTrans = row!!.findViewById(R.id.itemTypeTrans)
        }
    }
}