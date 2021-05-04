package com.example.quanlychitieu.ui.Home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quanlychitieu.api.*
import com.example.quanlychitieu.db.modeldb.WalletType
import com.example.quanlychitieu.repository.Repository
import com.google.gson.Gson
import kotlinx.coroutines.*

class HomeViewModel(val repository: Repository):ViewModel() {

    var infoUser:MutableLiveData<GetInfoCurrentUserResponseSuccess> = MutableLiveData()
    var listWallet:MutableLiveData<List<WalletInfo>> = MutableLiveData()


    fun getInfoUser(token:String):Deferred<GetInfoCurrentUserResponseSuccess?> = CoroutineScope(Dispatchers.Default).async{
        val repsonse = repository.getInfoCurrentUser(token)
        if(repsonse.isSuccessful()){
            Log.i("test", repsonse.body()!!.data.infoShow.email)
            repsonse.body()!!
        }
        else{
            null
        }
    }
    fun setInfoUser(token: String){
        CoroutineScope(Dispatchers.Default).launch {
            infoUser.postValue(getInfoUser(token).await())
        }
    }

    /**
     * function cho ViFragment
     */

    fun getListWallet(token: String):Deferred<List<WalletInfo>?> = CoroutineScope(Dispatchers.Default).async {
        val response = repository.getListWalletUser(token)
        if(response.isSuccessful()){
            response.body()!!.data.reuslt
        }
        else{
            null
        }
    }

    fun setListWallet(token: String) = CoroutineScope(Dispatchers.Default).launch{
        val listResult = getListWallet(token).await()
        listResult?.let {
            listWallet.postValue(listResult)
        }
    }

    fun loadingListWalletTypeFromSvToDb(token: String):Deferred<Boolean> = CoroutineScope(Dispatchers.Default).async{
        val response = repository.getListWalletType(token)
        if (response.isSuccessful()){
            repository.loadingListWalletTypeFromSv(response.body()!!.data.result)
        }
        else{
            false
        }
    }

    fun getListWalletFromDb():Deferred<List<WalletType>?> = CoroutineScope(Dispatchers.Default).async {
        val resultList = repository.getListWalletTypeFromDatabase()
        resultList
    }

    fun createWallet(token: String,newWallet:CreateWalletRequest):Deferred<Array<String>> = CoroutineScope(Dispatchers.Default).async{
        val response = repository.createNewWallet(token,newWallet)
        if(response.isSuccessful()){
            val result = response.body()!!
            arrayOf(result.code.toString(),result.data.message)
        }
        else{
            val gson = Gson()
            val createWalletResponseFail = gson.fromJson(
                response.errorBody()!!.string(),
                CreateWalletResponseFail::class.java
            )
            arrayOf(createWalletResponseFail.code.toString(),createWalletResponseFail.data.message)
        }
    }

    fun deleteWallet(token: String,typWallet:String):Deferred<Array<String>> = CoroutineScope(Dispatchers.Default).async {
        val response = repository.deleteWallet(token,typWallet)
        if(response.isSuccessful()){
            val result = response.body()!!
            arrayOf(result.code.toString(),result.data.message)
        }
        else{
            val gson = Gson()
            val deleteWalletResponse = gson.fromJson(
                response.errorBody()!!.string(),
                DeleteWalletResponse::class.java
            )
            arrayOf(deleteWalletResponse.code.toString(),deleteWalletResponse.data.message)
        }
    }

    fun updateWallet(token:String,typeWallet:String,amount:UpdateWalletRequest):Deferred<Array<String>> = CoroutineScope(Dispatchers.Default).async {
        val response = repository.updateWallet(token,typeWallet,amount)
        if(response.isSuccessful()){
            val result = response.body()!!
            arrayOf(result.code.toString(),result.data.message)
        }
        else{
            val gson = Gson()
            val updateWalletResponse = gson.fromJson(
                response.errorBody()!!.string(),
                UpdateWalletResponse::class.java
            )
            arrayOf(updateWalletResponse.code.toString(),updateWalletResponse.data.message)
        }
    }
    /**
     * function cho ThongKeFragment
     */

    /**
     * function cho GiaoDichFragment
     */

    fun getAllTransaction(token:String,type:String) =  viewModelScope.launch {
        val response= repository.getAllTransactions(token,type)
        if(response.isSuccessful()){
            allTrans.postValue(response.body()!!.data.result)
        }
        else null
    }
}