package com.example.fms.utils

import android.R
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.fms.ui.addTransactions.data.CategoryIdName
import com.example.fms.ui.addTransactions.data.SectionName
import com.example.fms.ui.addTransactions.data.WalletIdName


fun toast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun logs(text: String) {
    Log.v("CHOS", text)
}

fun spinnerCategory(context: Context, arrayList: ArrayList<CategoryIdName>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.simple_spinner_item, arrayList
    )
    adapter.setDropDownViewResource(R.layout.simple_spinner_item)
    spinner.adapter = adapter
}

fun spinnerWallet(context: Context, arrayList: ArrayList<WalletIdName>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.simple_spinner_item, arrayList
    )
    adapter.setDropDownViewResource(R.layout.simple_spinner_item)
    spinner.adapter = adapter
}

fun spinnerSection(context: Context, spinner: Spinner) {
    val sectionList: ArrayList<SectionName> = ArrayList()

    sectionList.add(SectionName("NEOBIS", "Neobis"))
    sectionList.add(SectionName("NEOLABS", "Neolabs"))

    val adapter = ArrayAdapter(
        context,
        R.layout.simple_spinner_dropdown_item, sectionList)
    adapter.setDropDownViewResource(R.layout.simple_spinner_item)
    spinner.adapter = adapter

}

