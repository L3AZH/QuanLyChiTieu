package com.example.quanlychitieu.db.modeldb

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "Transaction")
data class Transaction (
    @PrimaryKey val idTransaction:Int,
    val wallet_idWallet:Int,
    val transType_idTransType:Int,
    val amount:Double,
    val note:String,
    val date: Date
): Serializable {

}