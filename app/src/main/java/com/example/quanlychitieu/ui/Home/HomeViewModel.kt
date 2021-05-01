package com.example.quanlychitieu.ui.Home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quanlychitieu.api.GetInfoCurrentUserResponseSuccess
import com.example.quanlychitieu.repository.Repository
import kotlinx.coroutines.*

class HomeViewModel(val repository: Repository):ViewModel() {

    var infoUser:MutableLiveData<GetInfoCurrentUserResponseSuccess> = MutableLiveData()


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
}