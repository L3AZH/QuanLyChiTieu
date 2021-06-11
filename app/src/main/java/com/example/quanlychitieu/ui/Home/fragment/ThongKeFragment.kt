package com.example.quanlychitieu.ui.Home.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import com.example.quanlychitieu.R
import com.example.quanlychitieu.databinding.FragmentThongKeBinding
import com.example.quanlychitieu.ui.Home.HomeActivity
import com.example.quanlychitieu.ui.Home.HomeViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class ThongKeFragment : Fragment() {

    lateinit var binding: FragmentThongKeBinding
    lateinit var viewModel:HomeViewModel
    var c= Calendar.getInstance()
    val month=c.get(Calendar.MONTH)
    val year=c.get(Calendar.YEAR)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_thong_ke,container,false)
        viewModel = (activity as HomeActivity).homeViewModel
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        setUpChartChi()
        setUpPieChart()
    }
    /*fun setUpChartThu(){

        //var chonThang = item
        var amountThuVi1:Double
        var amountThuVi2:Double
        var amountThuVi3:Double
        var amountThuVi4:Double
        var amountThuVi5:Double
        var amountThuVi6:Double
        val sharePreference =
            requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharePreference.getString("accountToken", "null")
        val entries = ArrayList<BarEntry>()
        viewModel.allTransByUser.observe(viewLifecycleOwner, Observer { CoroutineScope(Dispatchers.Main).launch {
            amountThuVi1 = 0.0
            amountThuVi2 = 0.0
            amountThuVi3 = 0.0
            amountThuVi4 = 0.0
            amountThuVi5 = 0.0
            amountThuVi6 = 0.0
            for (i in 0 until viewModel.allTransByUser.value?.size!!){
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==1
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Thu")){
                    amountThuVi1+=viewModel.allTransByUser.value!!.get(i).amount
                }
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==2
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Thu")){
                    amountThuVi2+=viewModel.allTransByUser.value!!.get(i).amount
                }
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==3
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Thu")){
                    amountThuVi3+=viewModel.allTransByUser.value!!.get(i).amount
                }
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==4
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Thu")){
                    amountThuVi4+=viewModel.allTransByUser.value!!.get(i).amount
                }
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==5
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Thu")){
                    amountThuVi5+=viewModel.allTransByUser.value!!.get(i).amount
                }
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==6
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Thu")){
                    amountThuVi6+=viewModel.allTransByUser.value!!.get(i).amount
                }
            }

            entries.add(BarEntry(1f, amountThuVi1.toFloat()))
            entries.add(BarEntry(2f, amountThuVi2.toFloat()))
            entries.add(BarEntry(3f, amountThuVi3.toFloat()))
            entries.add(BarEntry(4f, amountThuVi4.toFloat()))
            entries.add(BarEntry(5f, amountThuVi5.toFloat()))
            entries.add(BarEntry(6f, amountThuVi6.toFloat()))
            entries.add(BarEntry(7f, 0f))
            val barDataSet = BarDataSet(entries, "")
            val data = BarData(barDataSet)
            binding.barChartThu.data = data // set the data and list of lables into chart
            binding.barChartThu.getXAxis().setDrawGridLines(false); // disable grid lines for the XAxis
            binding.barChartThu.getAxisLeft().setDrawGridLines(false); // disable grid lines for the left YAxis
            binding.barChartThu.getAxisRight().setDrawGridLines(false); // disable grid lines for the right YAxis
            binding.barChartThu.xAxis.isEnabled=false
            binding.barChartThu.axisRight.isEnabled=false
            binding.barChartThu.description.isEnabled=false
        } })
        viewModel.setListTransactionByUser(token!!)

    }*/
    fun setUpChartChi(){
        var amountChiVi1:Double
        var amountChiVi2:Double
        var amountChiVi3:Double
        var amountChiVi4:Double
        var amountChiVi5:Double
        var amountChiVi6:Double
        val sharePreference =
            requireActivity().getSharedPreferences("com.example.quanlychitieu", Context.MODE_PRIVATE)
        val token = sharePreference.getString("accountToken", "null")
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, 1f))
        entries.add(BarEntry(2f, 2f))
        entries.add(BarEntry(3f, 3f))
        entries.add(BarEntry(4f, 4f))
        entries.add(BarEntry(5f, 5f))
        entries.add(BarEntry(6f, 6f))
        entries.add(BarEntry(7f, 0f))
        val barDataSet = BarDataSet(entries, "")
        val data = BarData(barDataSet)
        binding.barChartChi.data = data // set the data and list of lables into chart
        binding.barChartChi.getXAxis().setDrawGridLines(false); // disable grid lines for the XAxis
        binding.barChartChi.getAxisLeft().setDrawGridLines(false); // disable grid lines for the left YAxis
        binding.barChartChi.getAxisRight().setDrawGridLines(false); // disable grid lines for the right YAxis
        binding.barChartChi.xAxis.isEnabled=false
        binding.barChartChi.axisRight.isEnabled=false
        binding.barChartChi.description.isEnabled=false
        viewModel.allTransByUser.observe(viewLifecycleOwner, Observer { CoroutineScope(Dispatchers.Main).launch {
            amountChiVi1 = 0.0
            amountChiVi2 = 0.0
            amountChiVi3 = 0.0
            amountChiVi4 = 0.0
            amountChiVi5 = 0.0
            amountChiVi6 = 0.0
            for (i in 0 until viewModel.allTransByUser.value?.size!!){
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==1
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Chi")){
                    amountChiVi1+=viewModel.allTransByUser.value!!.get(i).amount
                }
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==2
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Chi")){
                    amountChiVi2+=viewModel.allTransByUser.value!!.get(i).amount
                }
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==3
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Chi")){
                    amountChiVi3+=viewModel.allTransByUser.value!!.get(i).amount
                }
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==4
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Chi")){
                    amountChiVi4+=viewModel.allTransByUser.value!!.get(i).amount
                }
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==5
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Chi")){
                    amountChiVi5+=viewModel.allTransByUser.value!!.get(i).amount
                }
                if(viewModel.allTransByUser.value!!.get(i).wallet_idWallet==6
                    && fixYear(viewModel.allTransByUser.value!!.get(i).date.year)==year
                        && viewModel.allTransByUser.value!!.get(i).type.equals("Chi")){
                    amountChiVi6+=viewModel.allTransByUser.value!!.get(i).amount
                }
            }
            entries.add(BarEntry(1f, amountChiVi1.toFloat()))
            entries.add(BarEntry(2f, amountChiVi2.toFloat()))
            entries.add(BarEntry(3f, amountChiVi3.toFloat()))
            entries.add(BarEntry(4f, amountChiVi4.toFloat()))
            entries.add(BarEntry(5f, amountChiVi5.toFloat()))
            entries.add(BarEntry(6f, amountChiVi6.toFloat()))
            entries.add(BarEntry(7f, 0f))
            val barDataSet = BarDataSet(entries, "")
            val data = BarData(barDataSet)
            binding.barChartChi.data = data // set the data and list of lables into chart


        } })
        viewModel.setListTransactionByUser(token!!)

    }
    fun setUpPieChart(){
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(55F, "Chi tieu can thiet"))
        listColors.add(resources.getColor(R.color.green))
        listPie.add(PieEntry(10F, "Tiet kiem dai han"))
        listColors.add(resources.getColor(R.color.red))
        listPie.add(PieEntry(10F, "Quy giao duc"))
        listColors.add(resources.getColor(R.color.purple_700))
        listPie.add(PieEntry(10F, "Huong thu"))
        listColors.add(resources.getColor(R.color.purple_200))
        listPie.add(PieEntry(10F, "Tu do tai chinh"))
        listColors.add(resources.getColor(R.color.teal_200))
        listPie.add(PieEntry(5F, "Quy tu thien"))
        listColors.add(resources.getColor(R.color.humanskin))

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        binding.pieChart.data = pieData

        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.isDrawHoleEnabled = false
        binding.pieChart.description.isEnabled = false
        binding.pieChart.setEntryLabelColor(R.color.black)
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    fun fixYear(year: Int): Int {
        return (1900+year)
    }
}

