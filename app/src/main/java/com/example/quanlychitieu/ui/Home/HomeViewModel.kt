package com.example.quanlychitieu.ui.Home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlychitieu.api.*
import com.example.quanlychitieu.db.modeldb.BudgetRequestCodeIntent
import com.example.quanlychitieu.db.modeldb.TransType
import com.example.quanlychitieu.db.modeldb.Transaction
import com.example.quanlychitieu.db.modeldb.WalletType
import com.example.quanlychitieu.repository.Repository
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(val repository: Repository):ViewModel() {

    var infoUser:MutableLiveData<GetInfoCurrentUserResponseSuccess> = MutableLiveData()
    var listWallet:MutableLiveData<List<WalletInfo>> = MutableLiveData()
    var allTrans: MutableLiveData<List<TransInfoResponse>> = MutableLiveData()
    var listBudget:MutableLiveData<List<BudgetInfoResponse>> = MutableLiveData()
    var allTransByUser: MutableLiveData<List<TransInfoResponse>> = MutableLiveData()

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
    fun getAllTransactionByUser(token: String): Deferred<List<TransInfoResponse>?> =  CoroutineScope(Dispatchers.Default).async {
        val response= repository.getAllTransactionsByUser(token)
        if(response.isSuccessful()){
            response.body()!!.data.result
        }
        else {
            null
        }
    }

    fun setListTransactionByUser(token: String) = CoroutineScope(Dispatchers.Default).launch{

        val listResult = getAllTransactionByUser(token).await()
        listResult?.let {
            allTransByUser.postValue(listResult)
        }
    }

    /**
     * function cho GiaoDichFragment
     */

    fun getAllTransaction(token:String,type:String) =  viewModelScope.launch {
        val response= repository.getAllTransactions(token,type)
        if(response.isSuccessful()){
            allTrans.postValue(response.body()!!.data.result)
        }
        else {
            allTrans.postValue(null)
        }
    }

    fun getListTransTypeFromServer(token:String) : Deferred<Boolean> = CoroutineScope(Dispatchers.Default).async{
        val response=repository.getListTransTypeFromServer(token)
        if(response.isSuccessful){
            println(response.body()!!.data.result)
            repository.addListTransTypeToDB(response.body()!!.data.result)
        }
        else{
            false
        }
    }

    fun getListTransTypeFromDB():Deferred<List<TransType>?> = CoroutineScope(Dispatchers.Default).async {
        val list = repository.getListTransTypeFromDB()
        list
    }

    fun editTransaction(token:String,id:Int,updateTransaction: UpdateTransactionRequest): Deferred<Array<String>> = CoroutineScope(Dispatchers.Default).async{
        val result=repository.updateTransaction(token,id,updateTransaction)
        if(result.isSuccessful){
            val response = result.body()!!
            arrayOf(response.code.toString(),response.data.message)
        }
        else{
            val gson = Gson()
            val updateTransactionResponse = gson.fromJson(
                result.errorBody()!!.string(),
                UpdateTransactionResponse::class.java
            )
            arrayOf(updateTransactionResponse.code.toString(),updateTransactionResponse.data.message)
        }
    }

    fun deleteTransaction(token:String, transID:Int): Deferred<Array<String>> = CoroutineScope(Dispatchers.Default).async{
        val result=repository.deleteTransaction(token,transID)
        if(result.isSuccessful){
            val response = result.body()!!
            arrayOf(response.code.toString(),response.data.message)
        }
        else{
            val gson = Gson()
            val deleteTransactionResponse = gson.fromJson(
                result.errorBody()!!.string(),
                DeleteTransactionResponse::class.java
            )
            arrayOf(deleteTransactionResponse.code.toString(),deleteTransactionResponse.data.message)
        }
    }

    fun createTransaction(token:String,createTrans:CreateTransactionRequest): Deferred<Array<String>> = CoroutineScope(Dispatchers.Default).async {
        val result=repository.createTransaction(token,createTrans)
        println(result)
        if(result.isSuccessful){
            val response=result.body()!!
            arrayOf(response.code.toString(),response.data.message)
        }
        else{
            val gson=Gson()
            val createTransactionResponse=gson.fromJson(
                result.errorBody()!!.string(),
                CreateTransactionSuccessResponse::class.java
            )
            arrayOf(createTransactionResponse.code.toString(),createTransactionResponse.data.message)
        }
    }

    fun getAllBudget(token:String,idWallet: String) = CoroutineScope(Dispatchers.Default).async {
        val response=repository.getAllBudget(token,idWallet)
        if(response.isSuccessful()){
            listBudget.postValue(response.body()!!.data.result)
        }
        else{
            listBudget.postValue(null)
        }
    }

    fun createBudget(token:String, createBudgetRequest: CreateBudgetRequest):Deferred<Array<String>> = CoroutineScope(Dispatchers.Default).async{
        val result=repository.createBudget(token,createBudgetRequest)
        if(result.isSuccessful){
            val response = result.body()!!
            arrayOf(response.code.toString(),response.data.message, response.data.newBudgetId.toString())
        }
        else{
            val gson = Gson()
            val createBudgetInfoResponse = gson.fromJson(
                result.errorBody()!!.string(),
                CreateBudgetSuccessResponse::class.java
            )
            arrayOf(createBudgetInfoResponse.code.toString(),
                createBudgetInfoResponse.data.message)
        }
    }

    fun createBudgetRequestCode(budgetRequestCodeIntent: BudgetRequestCodeIntent) = CoroutineScope(Dispatchers.Default).launch{
        repository.insertBugetRequestCode(budgetRequestCodeIntent)
    }

    fun deleteBudgetRequestCode(budgetRequestCodeIntent: BudgetRequestCodeIntent) = CoroutineScope(Dispatchers.Default).launch{
        repository.deleteBugetRequestCode(budgetRequestCodeIntent)
    }

    fun getBudgetRequestCodeWithIdBudget(idBudget: String):BudgetRequestCodeIntent{
        return repository.getBugetRequestCodeIntent(idBudget);
    }



    fun updateBudget(token:String,idBudget:Int, updateBudgetRequest: UpdateBudgetRequest):Deferred<Array<String>> = CoroutineScope(Dispatchers.Default).async{
        val result=repository.updateBudget(token,idBudget,updateBudgetRequest)
        if(result.isSuccessful){
            val response = result.body()!!
            arrayOf(response.code.toString(),response.data.message)
        }
        else{
            val gson = Gson()
            val updateBudgetSuccessResponse = gson.fromJson(
                result.errorBody()!!.string(),
                UpdateBudgetSuccessResponse::class.java
            )
            arrayOf(updateBudgetSuccessResponse.code.toString(),updateBudgetSuccessResponse.data.message)
        }
    }

    fun deleteBudget(token:String, idBudget: Int):Deferred<Array<String>> = CoroutineScope(Dispatchers.Default).async {
        val result=repository.deleteBudget(token,idBudget)
        if(result.isSuccessful){
            val response = result.body()!!
            arrayOf(response.code.toString(),response.data.message)
        }
        else{
            val gson = Gson()
            val deleteBudgetResponse = gson.fromJson(
                result.errorBody()!!.string(),
                DeleteBudgetResponse::class.java
            )
            arrayOf(deleteBudgetResponse.code.toString(),deleteBudgetResponse.data.message)
        }
    }

    fun caculateDiffTime(dateOld: String, dateNew: String): Long? {
        try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val dateOldDate: Date = dateFormat.parse(dateOld)
            val dateNewDate: Date = dateFormat.parse(dateNew)
            val diff: Long = dateNewDate.time - dateOldDate.time
            val seconds = diff / 1000
            val minutes = diff / 60
            val hours = diff / 60
            val days = diff / 24
            Log.i("MyTime: ", "result: " + diff.toString());
            return diff
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}