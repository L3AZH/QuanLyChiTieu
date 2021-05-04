package com.example.quanlychitieu.repository

import com.example.quanlychitieu.api.*
import com.example.quanlychitieu.db.DbDAO
import com.example.quanlychitieu.db.modeldb.WalletType
import retrofit2.Response

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
    suspend fun deleteWallet(token: String,typeWallet:String) = RetrofitInstance.api.deleteWallet(token,typeWallet)
    suspend fun updateWallet(token: String,typeWallet:String,amount:UpdateWalletRequest) = RetrofitInstance.api.updateWallet(token,typeWallet,amount)
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
}