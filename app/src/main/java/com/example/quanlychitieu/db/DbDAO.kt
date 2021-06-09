package com.example.quanlychitieu.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quanlychitieu.db.modeldb.BudgetRequestCodeIntent
import com.example.quanlychitieu.db.modeldb.TransType
import com.example.quanlychitieu.db.modeldb.WalletType

@Dao
interface DbDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToListWalletType(walletType: WalletType):Long
    @Query("select * from WalletType")
    suspend fun getListWalletTypeFromDatabase():List<WalletType>
    //suspend fun getListTransactionType()
    @Query("Select * from TransType")
    suspend fun getListTransTypeFromDatabase():List<TransType>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListTransType(transType: TransType)

    @Insert
    suspend fun insertBudgetRequestCode(budgetRequestCode: BudgetRequestCodeIntent)
    @Delete
    suspend fun deteleBudgetRequestCode(budgetRequestCode: BudgetRequestCodeIntent)
    @Update
    suspend fun updateBudgetRequestCode(budgetRequestCode: BudgetRequestCodeIntent)
    @Query("select * from BudgetRequestCodeTable where idBudget = :idBudgetIn")
    fun getBudgetRequestCodeIntent(idBudgetIn:String): BudgetRequestCodeIntent
}