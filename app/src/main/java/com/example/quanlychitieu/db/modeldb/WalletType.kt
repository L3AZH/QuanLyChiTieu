package com.example.quanlychitieu.db.modeldb

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "WalletType")
data class WalletType(
    @PrimaryKey
    val idWalletType: Int,
    val type:String
):Serializable {
}