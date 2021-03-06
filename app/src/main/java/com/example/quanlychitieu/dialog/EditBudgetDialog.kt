package com.example.quanlychitieu.dialog

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.quanlychitieu.R
import com.example.quanlychitieu.api.BudgetInfoResponse
import com.example.quanlychitieu.api.UpdateBudgetRequest
import com.example.quanlychitieu.api.UpdateTransactionRequest
import com.example.quanlychitieu.databinding.DialogSuaBudgetBinding
import com.example.quanlychitieu.receiver.AlertReceiver
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.example.quanlychitieu.util.Constant
import com.example.quanlychitieu.util.RandomIntUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class EditBudgetDialog(val budgetInfoResponse: BudgetInfoResponse) :DialogFragment() {
    lateinit var binding:DialogSuaBudgetBinding
    lateinit var viewModel:HomeViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            var builder=AlertDialog.Builder(it)
            val inflater=requireActivity().layoutInflater
            binding=DataBindingUtil.inflate(inflater, R.layout.dialog_sua_budget,null,false)
            viewModel=(activity as HomeActivity).homeViewModel
            loadDetail()
            setButton()
            builder.setView(binding.root)
            builder.create()
        }?:throw IllegalStateException("Activity must not empty")
    }

    fun loadDetail(){
        binding.edtEditIDBudget.setText(budgetInfoResponse.idBudget.toString())
        binding.edtEditIDBudget.isEnabled=false

        binding.edtIdWallet.setText(budgetInfoResponse.walletIdWallet.toString())
        binding.edtIdWallet.isEnabled=false

        binding.edtEditAmount.setText(budgetInfoResponse.amount.toString())
        binding.tvDate.setText(budgetInfoResponse.date.toString())
        binding.edtEditNote.setText(budgetInfoResponse.note)
    }

    fun setButton(){
        var c=Calendar.getInstance()
        val year=budgetInfoResponse.date.year+1900
        val month=budgetInfoResponse.date.month
        val day=budgetInfoResponse.date.date
        c.set(budgetInfoResponse.date.year+1900,budgetInfoResponse.date.month,budgetInfoResponse.date.date)

        binding.btnPickDate.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val datePickerDialog = DatePickerDialog(requireContext(),
                    DatePickerDialog.OnDateSetListener { view, year, month, day ->
                        binding.tvDate.setText("" + year + "/" + (month + 1) + "/" + day)
                        c.set(year, month, day)
                    }, year, month, day
                )
                datePickerDialog.show()
            }
        }
        binding.btnEdtTrans.setOnClickListener {
            val sharedPreferences=requireActivity().getSharedPreferences("com.example.quanlychitieu",Context.MODE_PRIVATE)
            val token=sharedPreferences.getString("accountToken",null)
            CoroutineScope(Dispatchers.Default).async {
                //CheckField
                var amount=binding.edtEditAmount.text.toString()
                var note=binding.edtEditNote.text.toString().trim()
                if(checkField(amount,c)){
                    var updateBudget= UpdateBudgetRequest(amount.toDouble(),note,java.sql.Date(c.timeInMillis))
                    println(updateBudget)
                    val result=viewModel.updateBudget(token!!,budgetInfoResponse.idBudget,updateBudget).await()
                    if(result[0].equals("200")){
                        viewModel.getAllBudget(token,budgetInfoResponse.walletIdWallet.toString())
                        /**
                        Tuan anh them vao phan notification sau khi add thanh cong budget
                         */

                        val budgetRequestCodeIntent = viewModel.getBudgetRequestCodeWithIdBudget(binding.edtEditIDBudget.text.toString());
                        val alarmManagerDel = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val intentDel = Intent(context, AlertReceiver::class.java)
                        intentDel.action = budgetRequestCodeIntent.actionIntentType
                        val pendingIntentDel= PendingIntent.getBroadcast(context,budgetRequestCodeIntent.requestCodePendingIntent,intentDel,
                            PendingIntent.FLAG_UPDATE_CURRENT)
                        alarmManagerDel.cancel(pendingIntentDel)


                        val currentDay =
                            DateFormat.format("dd/MM/yyyy HH:mm", Calendar.getInstance().time).toString()
                        val setDay = DateFormat.format("dd/MM/yyyy HH:mm", updateBudget.date.time).toString()
                        Log.i("MyTime", "currentDay " + currentDay + " -------Set Day: " + setDay)
                        val diff = viewModel.caculateDiffTime(currentDay, setDay)
                        val alarmManager =
                            requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val intent = Intent(context, AlertReceiver::class.java)
                        intent.action = Constant.ACTION_SET_EXACT_ALARM // set daily default
                        intent.putExtra(Constant.EXTRA_TITLE, "Note: "+updateBudget.note + "\nDay:"+ setDay)
                        intent.putExtra(
                            Constant.EXTRA_DECRIPTION,
                            updateBudget.amount.toString()
                        )
                        val getRequestCodePendingIntent = RandomIntUtil.getRandom()
                        intent.putExtra(Constant.EXTRA_REQUESTCODE_PENDING, getRequestCodePendingIntent)
                        val pendingIntent = PendingIntent.getBroadcast(
                            context,
                            getRequestCodePendingIntent,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        alarmManager?.let {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // s??? d???ng dozen mode, google android doc => b??o khi ???? b???t ti???t ki???m pin
                                it.setExactAndAllowWhileIdle(
                                    AlarmManager.RTC_WAKEUP,
                                    Calendar.getInstance().timeInMillis + diff!!,
                                    pendingIntent
                                )
                            } else {
                                alarmManager.setExact(
                                    AlarmManager.RTC_WAKEUP,
                                    Calendar.getInstance().timeInMillis + diff!!,
                                    pendingIntent
                                )
                            }
                        }
                        dialog?.cancel()
                    }
                    else{
                        Snackbar.make(binding.root,result[1], Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
        binding.btnDeleteTrans.setOnClickListener {
            val sharedPreferences=requireActivity().getSharedPreferences("com.example.quanlychitieu",Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("accountToken",null)
            CoroutineScope(Dispatchers.Default).launch{
                //Check field
                var id=budgetInfoResponse.idBudget
                var idWallet=budgetInfoResponse.walletIdWallet.toString()
                val result=viewModel.deleteBudget(token!!,id).await()
                /**
                 * Tuan anh part
                 */
                val budgetRequestCodeIntent = viewModel.getBudgetRequestCodeWithIdBudget(binding.edtEditIDBudget.text.toString())
                val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, AlertReceiver::class.java)
                intent.action = budgetRequestCodeIntent.actionIntentType
                val pendingIntent = PendingIntent.getBroadcast(context,budgetRequestCodeIntent.requestCodePendingIntent,intent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.cancel(pendingIntent)
                viewModel.deleteBudgetRequestCode(budgetRequestCodeIntent)
                /**
                 *
                 */
                if(result[0].equals("200")){
                    viewModel.getAllBudget(token,idWallet)
                    dialog?.cancel()
                }
                else{
                    Snackbar.make(binding.root,result[1], Snackbar.LENGTH_LONG).show()
                }
            }
        }
        binding.btnEdtCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    fun checkField(amount:String,c: Calendar):Boolean{
        if(amount.isEmpty() || amount==""){
            Toast.makeText(context,"Vui l??ng nh???p s??? ti???n", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(amount.toDouble()<1000 || amount.toDouble() >100000000000){
            Toast.makeText(context,"S??? ti???n kh??ng h???p l???", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(java.sql.Date(c.timeInMillis) < java.sql.Date(System.currentTimeMillis()) ){
            Toast.makeText(context,"Kh??ng ???????c ch???n ng??y ??? qu?? kh???", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}