package com.example.neofin.utils

import android.app.DatePickerDialog
import com.example.neofin.R
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.neofin.ui.addTransactions.data.CategoryIdName
import com.example.neofin.ui.addTransactions.data.SectionName
import com.example.neofin.ui.addTransactions.data.WalletIdName
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private val calendar = Calendar.getInstance()

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
        R.layout.spinner, sectionList)

    spinner.adapter = adapter

}

fun date(text: TextView) : DatePickerDialog.OnDateSetListener {
    return DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        text!!.text = sdf.format(calendar.time)
    }
}


