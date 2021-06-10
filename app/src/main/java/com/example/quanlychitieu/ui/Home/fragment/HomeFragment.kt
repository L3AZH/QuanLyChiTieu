package com.example.quanlychitieu.ui.Home.fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.quanlychitieu.R
import com.example.quanlychitieu.databinding.FragmentHomeBinding
import com.example.quanlychitieu.db.modeldb.Transaction
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.example.quanlychitieu.ui.LoginRegister.LoginAndRegisterActivity
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    lateinit var binding:FragmentHomeBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        viewModel = (activity as HomeActivity).homeViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInfoUser()
        setOnClickInfoBtn()
        setUpChart()
    }
    fun setUpInfoUser(){
        val sharePreference =
            requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharePreference.getString("accountToken", "null")
        viewModel.infoUser.observe(viewLifecycleOwner, Observer {
            CoroutineScope(Dispatchers.Main).launch {
                binding.emailAccountTextView.text = "Email: "+viewModel.infoUser.value?.data?.infoShow?.email
                binding.usernameAccountTextView.text = "User name: "+viewModel.infoUser.value?.data?.infoShow?.username
                binding.phoneAccountTextView.text = "SDT: "+viewModel.infoUser.value?.data?.infoShow?.phone
                binding.joindateAccountTextView.text = "Ngay tao: "+viewModel.infoUser.value?.data?.infoShow?.joindate
            }
        })
        viewModel.setInfoUser(token!!)
    }
    fun setUpChart(){
        var c= Calendar.getInstance()
        val month=c.get(Calendar.MONTH)
        var amountThuByMonth:Double
        var amountChiByMonth:Double
        val sharePreference =
            requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharePreference.getString("accountToken", "null")
        val entries = ArrayList<BarEntry>()
        viewModel.allTransByUser.observe(viewLifecycleOwner, Observer { CoroutineScope(Dispatchers.Main).launch {
            amountThuByMonth=0.0
            amountChiByMonth=0.0
            for (i in 0 until viewModel.allTransByUser.value?.size!!){
                if(viewModel.allTransByUser.value!!.get(i).type.equals("Thu") && viewModel.allTransByUser.value!!.get(i).date.month==month){
                    amountThuByMonth+=viewModel.allTransByUser.value!!.get(i).amount
                }
                if(viewModel.allTransByUser.value!!.get(i).type.equals("Chi") && viewModel.allTransByUser.value!!.get(i).date.month==month){
                    amountChiByMonth+=viewModel.allTransByUser.value!!.get(i).amount
                }
            }
            binding.tvThuHome.text = DecimalFormat("##.##").format(amountThuByMonth).toString()
            binding.tvChiHome.text = DecimalFormat("##.##").format(amountChiByMonth).toString()
            binding.tvKqHome.text = DecimalFormat("##.##").format(amountThuByMonth-amountChiByMonth).toString()
            print(amountThuByMonth)
            print(amountChiByMonth)

        } })

        entries.add(BarEntry(3f, 0f))
        entries.add(BarEntry(1f, binding.tvThuHome.text.toString().toFloat()))
        entries.add(BarEntry(2f, binding.tvChiHome.text.toString().toFloat()))
        println(entries)
        val barDataSet = BarDataSet(entries, "")

        val data = BarData(barDataSet)
        println(data)
        binding.barChart.data = data // set the data and list of lables into chart
        viewModel.setListTransactionByUser(token!!)
        binding.barChart.getXAxis().setDrawGridLines(false); // disable grid lines for the XAxis
        binding.barChart.getAxisLeft().setDrawGridLines(false); // disable grid lines for the left YAxis
        binding.barChart.getAxisRight().setDrawGridLines(false); // disable grid lines for the right YAxis
        binding.barChart.xAxis.isEnabled=false
        binding.barChart.axisRight.isEnabled=false
        binding.barChart.description.isEnabled=false
    }
    fun setOnClickInfoBtn(){
        binding.infoImgViewBtn.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.cardViewInfo,AutoTransition())
            if(binding.emailAccountTextView.isVisible){
                binding.usernameAccountTextView.visibility = View.GONE
                binding.emailAccountTextView.visibility = View.GONE
                binding.phoneAccountTextView.visibility = View.GONE
                binding.joindateAccountTextView.visibility = View.GONE
                binding.linearLayoutBtnInfo.visibility = View.GONE
            }
            else{
                binding.usernameAccountTextView.visibility = View.VISIBLE
                binding.emailAccountTextView.visibility = View.VISIBLE
                binding.phoneAccountTextView.visibility = View.VISIBLE
                binding.joindateAccountTextView.visibility = View.VISIBLE
                binding.linearLayoutBtnInfo.visibility = View.VISIBLE
            }
        }
        binding.homeLogoutBtn.setOnClickListener {
            val sharePreference =
                requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
            sharePreference.edit().putString("accountToken",null).apply()
            val goToLoginScreen = Intent(activity,LoginAndRegisterActivity::class.java)
            startActivity(goToLoginScreen)
        }
    }
}