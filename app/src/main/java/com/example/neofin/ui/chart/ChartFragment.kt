package com.example.neofin.ui.chart

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.analytics.Analytics
import com.example.neofin.ui.chart.data.SectionBalance
import com.example.neofin.ui.filter.data.Period
import com.example.neofin.ui.filter.data.TransactionType
import com.example.neofin.utils.logs
import com.example.neofin.utils.spinnerPeriodAnalytics
import com.example.neofin.utils.spinnerTransactionAnalytics
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_chart.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChartFragment : Fragment(R.layout.fragment_chart) {
    var neobis = 0
    var neolabs = 0
    var date2 = ""
    var type2 = 0

    private val entries: ArrayList<PieEntry> = ArrayList()
    private val entriesBar: ArrayList<BarEntry> = ArrayList()
    private val labelsBar: ArrayList<String> = ArrayList()
    val test: ArrayList<SectionBalance> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.hide()

        pieDiagram.setNoDataText("Загрузка диаграммы...")
        pieDiagram.invalidate()

        bar.setNoDataText("Нажмите на организацию на верхней диаграмме!")
        bar.invalidate()

        backBT.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_home
            )
        }

        getDate()

        getType()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupPieChart() {
        pieDiagram.isDrawHoleEnabled = false
        pieDiagram.setUsePercentValues(true)
        pieDiagram.description.text = ""
        pieDiagram.invalidate()
        pieDiagram.notifyDataSetChanged()
        pieDiagram.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (h?.x!! <= 0.0) {
                    getCategoryAnalytics(
                        date2.substringAfter(' '), 0,
                        date2.substringBefore(' '), type2)
                } else {
                    getCategoryAnalytics(
                        date2.substringAfter(' '), 1,
                        date2.substringBefore(' '), type2)
                }
            }

            override fun onNothingSelected() {}
        })
        entries.clear()
        entries.add(PieEntry(neobis.toFloat(), "Neobis"))
        entries.add(PieEntry(neolabs.toFloat(), "Neolabs"))
        val colors: ArrayList<Int> = ArrayList()

        colors.add(Color.parseColor("#1778E9"))
        colors.add(Color.parseColor("#094387"))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieDiagram))
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.WHITE)

        pieDiagram.data = data

        pieDiagram.animateY(1000, Easing.EaseInOutQuad)
    }

    private fun setupBarTest() {
        entriesBar.clear()
        labelsBar.clear()

        for (i in test.indices) {
            val label = test[i].name
            val balance = test[i].amount
            entriesBar.add(BarEntry(i.toFloat(), balance.toFloat()))
            labelsBar.add(label)
        }

        val barDataSet = BarDataSet(entriesBar, "")
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#1778E9"))
        barDataSet.colors = colors
        bar.description.text = "Категории"
        val barData= BarData(barDataSet)
        bar.data = barData

        val xAxis: XAxis = bar.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labelsBar)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelCount = labelsBar.size
        xAxis.labelRotationAngle = 360F
        bar.animateY(50)
        bar.notifyDataSetChanged()
        bar.invalidate()
    }

    private fun getDate(){
        spinnerPeriodAnalytics(requireContext(), periodAnalytics)
        periodAnalytics.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                val period: Period = parent.selectedItem as Period
                date2 = period.period
                getSectionAnalytics(period.period.substringAfter(' '), 0, period.period.substringBefore(' '), type2)
                getSectionAnalytics(period.period.substringAfter(' '), 1, period.period.substringBefore(' '), type2)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun getType(){
        spinnerTransactionAnalytics(requireContext(), typeAnalytics)
        typeAnalytics.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                val type: TransactionType = parent.selectedItem as TransactionType
                type2 = type.id
                getSectionAnalytics(date2.substringAfter(' '), 0, date2.substringBefore(' '), type.id)
                getSectionAnalytics(date2.substringAfter(' '), 1, date2.substringBefore(' '), type.id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun getSectionAnalytics(
        endData: String,
        neoSection: Int,
        startData: String,
        transactionType: Int
    ) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token  = RetrofitBuilder.getToken()
        retIn.getAnalytics(token, endData, neoSection, startData, transactionType).enqueue(object :
            Callback<Analytics> {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onResponse(call: Call<Analytics>, response: Response<Analytics>) {
                if (response.isSuccessful) {
                    if (neoSection == 0) {
                        neobis = response.body()?.totalBalance!!
                    } else {
                        neolabs = response.body()?.totalBalance!!

                    }
                    if (pieDiagram != null) {
                        setupPieChart()
                    } else {
                        logs("pieDiagram is null")
                    }
                } else {
                    logs("Error in Charts, getSectionAnalytics")
                }
            }

            override fun onFailure(call: Call<Analytics>, t: Throwable) {
            }

        })
    }

    private fun getCategoryAnalytics(
        endData: String,
        neoSection: Int,
        startData: String,
        transactionType: Int
    ) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token  = RetrofitBuilder.getToken()
        retIn.getAnalytics(token, endData, neoSection, startData, transactionType).enqueue(object :
            Callback<Analytics> {
            override fun onResponse(call: Call<Analytics>, response: Response<Analytics>) {
                if (response.isSuccessful) {
                    test.clear()
                    response.body()?.details?.forEach {
                        test.add(SectionBalance(it.id, it.name, it.amount))
                        setupBarTest()

                    }
                } else {
                    logs("Error in Charts, getCategoryAnalytics")
                }
            }

            override fun onFailure(call: Call<Analytics>, t: Throwable) {
            }

        })
    }
}