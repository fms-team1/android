package com.example.neofin.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.neofin.R
import com.example.neofin.ui.addTransactions.data.CategoryIdName
import com.example.neofin.ui.addTransactions.data.SectionName
import com.example.neofin.ui.addTransactions.data.WalletIdName
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private val calendar = Calendar.getInstance()
@SuppressLint("SimpleDateFormat")
private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

fun toast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun logs(text: String) {
    Log.v("CHOS", text)
}

fun spinnerCategory(context: Context, arrayList: ArrayList<CategoryIdName>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.spinner, arrayList
    )

    spinner.adapter = adapter
}

fun spinnerWallet(context: Context, arrayList: ArrayList<WalletIdName>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.spinner, arrayList
    )

    spinner.adapter = adapter
}

fun spinnerSection(context: Context, spinner: Spinner) {
    val sectionList: ArrayList<SectionName> = ArrayList()

    sectionList.add(SectionName("NEOBIS", "Neobis"))
    sectionList.add(SectionName("NEOLABS", "Neolabs"))

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner, sectionList
    )

    spinner.adapter = adapter

}

fun date(text: TextView) : DatePickerDialog.OnDateSetListener {
    return DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        text!!.text = sdf.format(calendar.time)
    }
}

fun getMonth(): String {
    val myFormat = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    val currentDate = sdf.format(Date())
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, -1)
    val date = calendar.time
    val dateOutput = sdf.format(date)

    return "$dateOutput $currentDate"
}

fun getWeek(): String {
    val myFormat = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    val currentDate = sdf.format(Date())
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -7)
    val date = calendar.time
    val dateOutput = sdf.format(date)

    return "$dateOutput $currentDate"
}

fun getYear(): String {
    val myFormat = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    val currentDate = sdf.format(Date())
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.YEAR, -1)
    val date = calendar.time
    val dateOutput = sdf.format(date)

    return "$dateOutput $currentDate"
}


