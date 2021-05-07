package com.example.quanlychitieu.db.modeldb

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "TransType")
data class TransType(
    @PrimaryKey val idTransType:Int,
    val type:String,
    val catergory:String
):Serializable{

}