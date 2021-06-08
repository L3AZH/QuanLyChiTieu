package com.example.quanlychitieu.db.modeldb

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "BudgetRequestCodeTable")
data class BudgetRequestCodeIntent(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val description:String,
    val time:String,
    val date:String,
    val requestCodePendingIntent: Int,
    val actionIntentType:String)