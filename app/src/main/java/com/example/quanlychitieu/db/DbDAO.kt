package com.example.quanlychitieu.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quanlychitieu.db.modeldb.WalletType

@Dao
interface DbDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToListWalletType(walletType: WalletType):Long
    @Query("select * from WalletType")
    suspend fun getListWalletTypeFromDatabase():List<WalletType>
    //suspend fun getListTransactionType()
}