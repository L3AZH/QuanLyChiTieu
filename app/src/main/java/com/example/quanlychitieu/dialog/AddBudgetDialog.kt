package com.example.quanlychitieu.dialog

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.quanlychitieu.R
import com.example.quanlychitieu.api.CreateBudgetRequest
import com.example.quanlychitieu.databinding.DialogAddBudgetBinding
import com.example.quanlychitieu.db.modeldb.BudgetRequestCodeIntent
import com.example.quanlychitieu.receiver.AlertReceiver
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.example.quanlychitieu.util.Constant
import com.example.quanlychitieu.util.RandomIntUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.*

class AddBudgetDialog (val idWallet:String): DialogFragment() {
    lateinit var binding:DialogAddBudgetBinding
    lateinit var viewModel:HomeViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val builder=AlertDialog.Builder(it)
            val inflater=requireActivity().layoutInflater
            binding=DataBindingUtil.inflate(inflater, R.layout.dialog_add_budget,null,false)
            viewModel=(activity as HomeActivity).homeViewModel
            setupButton()
            builder.setView(binding.root)
            builder.create()
        }?:throw IllegalStateException("Activity must not empty")
    }

    fun setupButton(){
        var c=Calendar.getInstance()
        val year=c.get(Calendar.YEAR)
        val month=c.get(Calendar.MONTH)
        val day=c.get(Calendar.DATE)

        binding.btnPickDate.setOnClickListener{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val datePickerDialog= DatePickerDialog(requireContext(),
                    DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    binding.tvDate.setText(""+year+"/"+(month+1)+"/"+day)
                    c.set(year,month,day)
                },year,month,day)
                datePickerDialog.show()
            }
        }
        binding.btnSaveBudget.setOnClickListener {
            var amount=binding.edtAddAmount.text.toString()
            var note=binding.edtAddNote.text.toString()
            if(checkField(amount,c)){
                val sharedPreferences=requireActivity().getSharedPreferences("com.example.quanlychitieu",
                    Context.MODE_PRIVATE)
                val token=sharedPreferences.getString("accountToken",null)
                var budgetRequest=CreateBudgetRequest(idWallet.toInt(),amount.toDouble(),note,java.sql.Date(c.timeInMillis))
                CoroutineScope(Dispatchers.Default).async {
                    val result=viewModel.createBudget(token!!,budgetRequest).await()
                    if(result[0]=="200"){
                        viewModel.getAllBudget(token,idWallet)
                        /**
                        Tuan anh them vao phan notification sau khi add thanh cong budget
                         */
                        val currentDay =
                            DateFormat.format("dd/MM/yyyy HH:mm", Calendar.getInstance().time).toString()
                        val setDay = DateFormat.format("dd/MM/yyyy HH:mm", budgetRequest.date.time).toString()
                        Log.i("MyTime", "currentDay " + currentDay + " -------Set Day: " + setDay)
                        val diff = viewModel.caculateDiffTime(currentDay, setDay)
                        val alarmManager =
                            requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val intent = Intent(context, AlertReceiver::class.java)
                        intent.action = Constant.ACTION_SET_EXACT_ALARM // set daily default
                        val actionIntentType = intent.action
                        intent.putExtra(Constant.EXTRA_TITLE, "Note: "+budgetRequest.note + "\nDay:"+ setDay)
                        intent.putExtra(
                            Constant.EXTRA_DECRIPTION,
                            budgetRequest.amount.toString()
                        )
                        val getRequestCodePendingIntent = RandomIntUtil.getRandom()
                        intent.putExtra(Constant.EXTRA_REQUESTCODE_PENDING, getRequestCodePendingIntent)
                        /*
                        * pendingrequestcode phải khác nhau để định danh các báo thức ko là sẽ bị ghi chồng báo thức
                        * */
                        val pendingIntent = PendingIntent.getBroadcast(
                            context,
                            getRequestCodePendingIntent,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        /*
                        *  believe the error is here:

                alarm.SetExact(int type, long triggerAtMillis, PendingIntent operation);
                triggerAtMillis: time in milliseconds that the alarm should go off, using the appropriate clock (depending on the alarm type).

                So, your are using 1800000 as triggerAtMillis. However, 1800000 is following date in UTC: Thu Jan 01 1970 00:30:00

                Since this is an old date, the alarm is fired immediately.

                Solution

                Maybe, you should update your code as follows:

                In MainActivity, I believe that you want to fire the alarm immediately. So, create it as follows:

                alarm.SetExact(AlarmType.RtcWakeup, Calendar.getInstance().getTimeInMillis(), pending);
                In your service, it seems that you want to trigger your alarm after 1800000. So, you have to use:

                alarm.SetExact(AlarmType.RtcWakeup, Calendar.getInstance().getTimeInMillis() + LOCATION_INTERVAL, pending);
                This way, alarm will be fired 30 minutes after current time (current time + LOCATION_INTERVAL).

                Keep in mind that second parameter is the date in milliseconds... It is a number which represents an whole date (and not only an interval)...
                        * */
                        alarmManager?.let {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                // sử dụng dozen mode, google android doc => báo khi đã bật tiết kiệm pin
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
                        viewModel.createBudgetRequestCode(
                            BudgetRequestCodeIntent(
                                0,
                                budgetRequest.note,
                                budgetRequest.amount.toString(),
                                "00:00",
                                setDay,
                                getRequestCodePendingIntent,
                                actionIntentType!!
                            )
                        )
                        dialog?.cancel()
                    }
                    else{
                        Snackbar.make(binding.root,result[1], Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    fun checkField(amount:String,c: Calendar):Boolean{
        if(amount.isEmpty() || amount==""){
            Toast.makeText(context,"Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(amount.toDouble()<1000 || amount.toDouble() >100000000000){
            Toast.makeText(context,"Số tiền không hợp lệ", Toast.LENGTH_SHORT).show()
            return false
        }
        else if(java.sql.Date(c.timeInMillis) < java.sql.Date(System.currentTimeMillis()) ){
            Toast.makeText(context,"Không được chọn ngày ở quá khứ", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}