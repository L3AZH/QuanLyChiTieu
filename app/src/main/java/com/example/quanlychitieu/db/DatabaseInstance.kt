package com.example.quanlychitieu.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quanlychitieu.db.modeldb.TransType
import com.example.quanlychitieu.db.modeldb.WalletType

@Database(entities = [WalletType::class, TransType::class],version = 3)
abstract class DatabaseInstance:RoomDatabase() {
    abstract fun getDbDao():DbDAO

    companion object{
        @Volatile
        private var instance:DatabaseInstance?=null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?: createDatebase(context).also {
                Log.i("QLTC_Database","set up create database first time")
                instance = it
            }
        }
        private fun createDatebase(context: Context):DatabaseInstance{
            return Room.databaseBuilder(
                context.applicationContext,
                DatabaseInstance::class.java,
                "QLTC_database.db"
            ).build()
        }
    }
}