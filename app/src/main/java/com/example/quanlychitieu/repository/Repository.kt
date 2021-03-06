package com.example.quanlychitieu.repository

import androidx.lifecycle.LiveData
import com.example.quanlychitieu.api.*
import com.example.quanlychitieu.db.DbDAO
import com.example.quanlychitieu.db.modeldb.BudgetRequestCodeIntent
import com.example.quanlychitieu.db.modeldb.TransType
import com.example.quanlychitieu.db.modeldb.Transaction
import com.example.quanlychitieu.db.modeldb.WalletType

class Repository(val dbDAO: DbDAO) {
    /*
    * View Model se lay ham trong day su dung
    * */
    /**
     * lay data tren server theo api
     */
    suspend fun login(account:LoginRequest) = RetrofitInstance.api.login(account)
    suspend fun register(account: RegisterRequest) = RetrofitInstance.api.register(account)
    suspend fun getInfoCurrentUser(token:String) = RetrofitInstance.api.getInfoAccount(token)
    suspend fun getListWalletUser(token: String) = RetrofitInstance.api.getListWalletUser(token)
    suspend fun getListWalletType(token: String) = RetrofitInstance.api.getListWalletType(token)
    suspend fun createNewWallet(token: String,walletNew:CreateWalletRequest) = RetrofitInstance.api.createNewWallet(token,walletNew)
    suspend fun getAllTransactions(token:String,type:String)=RetrofitInstance.api.getAllTransaction(token,type)
    suspend fun getAllTransactionsByUser(token:String)=RetrofitInstance.api.getAllTransactionByUser(token)
    suspend fun updateTransaction(token:String,idTrans: Int,updateTrans:UpdateTransactionRequest)=RetrofitInstance.api.updateTransaction(token,idTrans,updateTrans)
    suspend fun deleteTransaction(token:String,idTrans: Int)= RetrofitInstance.api.deleteTransaction(token,idTrans)
    suspend fun getListTransTypeFromServer(token: String) = RetrofitInstance.api.getAllTransType(token)
    suspend fun createTransaction(token:String,trans:CreateTransactionRequest)=RetrofitInstance.api.createTransaction(token,trans)
    suspend fun getAllBudget(token:String,idWallet:String)=RetrofitInstance.api.getAllBudget(token,idWallet)
    suspend fun createBudget(token:String,createBudgetRequest: CreateBudgetRequest)=RetrofitInstance.api.createBudget(token,createBudgetRequest)
    suspend fun updateBudget(token:String,idBudget:Int,updateBudgetRequest: UpdateBudgetRequest)=RetrofitInstance.api.updateBudget(token,idBudget,updateBudgetRequest)
    suspend fun deleteBudget(token:String,idBudget:Int)=RetrofitInstance.api.deleteBudget(token,idBudget)

    suspend fun deleteWallet(token: String,typeWallet:String) = RetrofitInstance.api.deleteWallet(token,typeWallet)
    suspend fun updateWallet(token: String,typeWallet:String,amount: UpdateWalletRequest) = RetrofitInstance.api.updateWallet(token,typeWallet,amount)
    /**
     * lay data trong db
     */

    suspend fun getListWalletTypeFromDatabase():List<WalletType>{
        return dbDAO.getListWalletTypeFromDatabase()
    }
    suspend fun loadingListWalletTypeFromSv(listWalletType:List<WalletType>):Boolean{
        var count = 0
        for (item in listWalletType){
            dbDAO.addToListWalletType(item)
            count++
        }
        if (count == listWalletType.size) return true
        return false
    }

    suspend fun addListTransTypeToDB(listTransType: List<TransType>):Boolean{
        for (item in listTransType){
            try{
                dbDAO.insertListTransType(item)
            }
            catch(e:Exception){
                e.printStackTrace()
                return false
            }
        }
        return true
    }
    suspend fun getListTransTypeFromDB():List<TransType>{
        return dbDAO.getListTransTypeFromDatabase()
    }

    suspend fun insertBugetRequestCode(budgetRequestCode: BudgetRequestCodeIntent){
        return dbDAO.insertBudgetRequestCode(budgetRequestCode)
    }
    suspend fun deleteBugetRequestCode(budgetRequestCode: BudgetRequestCodeIntent){
        return dbDAO.deteleBudgetRequestCode(budgetRequestCode)
    }
    suspend fun updateBugetRequestCode(budgetRequestCode: BudgetRequestCodeIntent){
        return dbDAO.updateBudgetRequestCode(budgetRequestCode)
    }
    fun getBugetRequestCodeIntent(idBudget:String): BudgetRequestCodeIntent {
        return dbDAO.getBudgetRequestCodeIntent(idBudget);
    }

}