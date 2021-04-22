package com.example.neofin.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.*
import com.example.neofin.R
import com.example.neofin.ui.addTransactions.data.CategoryIdName
import com.example.neofin.ui.addTransactions.data.SectionName
import com.example.neofin.ui.addTransactions.data.WalletIdName
import com.example.neofin.ui.filter.data.*
import com.example.neofin.ui.journal.journalById.data.CategoryItem
import com.example.neofin.ui.journal.journalById.data.WalletItem
import com.example.neofin.ui.user.data.Section
import com.example.neofin.ui.user.data.StatusName
import com.example.neofin.ui.user.data.Type
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_filters.*
import java.text.ParseException
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

fun snackbar(view: View, text: String, color: Int) {
    val snackBar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT
    ).setAction("Action", null)
    snackBar.setActionTextColor(Color.WHITE)
    val snackBarView = snackBar.view
    snackBarView.setBackgroundColor(color)
    val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(Color.WHITE)
    textView.textSize = 16F
    textView.maxLines = 6
    snackBar.show()
}

fun spinnerGroup(context: Context, spinner: Spinner) {
    val group: ArrayList<String> = ArrayList()

    group.add("Выберите опцию")
    group.add("Создать группу")
    group.add("Выбрать из существующих")

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner, group
    )

    spinner.adapter = adapter
}

fun spinnerArchive(context: Context, spinner: Spinner) {
    val group: ArrayList<String> = ArrayList()

    group.add("Выберите опцию")
    group.add("Кошелек")
    group.add("Группа")
    group.add("Категория")
    group.add("Пользователь")

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner, group
    )

    spinner.adapter = adapter
}

fun spinnerStatusUser(context: Context, spinner: Spinner?) {
    val sectionList: ArrayList<StatusName> = ArrayList()

    sectionList.add(StatusName(null, "Статус"))
    sectionList.add(StatusName("NEW", "Новый"))
    sectionList.add(StatusName("APPROVED", "Одобрить"))
    sectionList.add(StatusName("BLOCKED", "Блокировать"))
    sectionList.add(StatusName("ARCHIVED", "Архивировать"))

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner, sectionList
    )

    spinner?.adapter = adapter
}

fun spinnerSectionUser(context: Context, spinner: Spinner) {
    val sectionList: ArrayList<Section> = ArrayList()

    sectionList.add(Section("NEOBIS", "Neobis"))
    sectionList.add(Section("NEOLABS", "Neolabs"))

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner, sectionList
    )

    spinner.adapter = adapter
}

fun spinnerTypeUser(context: Context, spinner: Spinner) {
    val typeList: ArrayList<Type> = ArrayList()

    typeList.add(Type("INCOME", "Доход"))
    typeList.add(Type("EXPENSE", "Расход"))

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner, typeList
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

fun spinnerWalletChange(context: Context, arrayList: ArrayList<WalletItem>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, arrayList
    )

    spinner.adapter = adapter
}

fun spinnerSection(context: Context, spinner: Spinner) {
    val sectionList: ArrayList<SectionName> = ArrayList()

    sectionList.add(SectionName(-1, "Выберите организацию"))
    sectionList.add(SectionName(0, "Neobis"))
    sectionList.add(SectionName(1, "Neolabs"))

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner, sectionList
    )

    spinner.adapter = adapter
}

fun spinnerCategory(context: Context, arrayList: ArrayList<CategoryIdName>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.spinner, arrayList
    )

    spinner.adapter = adapter
}

fun spinnerUserFilter(context: Context, arrayList: ArrayList<UserIdName>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, arrayList
    )

    spinner.adapter = adapter
}

fun spinnerWalletFilterTo(context: Context, arrayList: ArrayList<WalletIdNameTo>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, arrayList
    )

    spinner.adapter = adapter
}

fun spinnerWalletFilterFrom(context: Context, arrayList: ArrayList<WalletIdNameFrom>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, arrayList
    )

    spinner.adapter = adapter
}

fun spinnerWalletFilter(context: Context, arrayList: ArrayList<WalletIdName>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, arrayList
    )

    spinner.adapter = adapter
}

fun spinnerSectionFilter(context: Context, spinner: Spinner) {
    val sectionList: ArrayList<SectionName> = ArrayList()

    sectionList.add(SectionName(-1, "Организация"))
    sectionList.add(SectionName(0, "Neobis"))
    sectionList.add(SectionName(1, "Neolabs"))

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, sectionList
    )

    spinner.adapter = adapter

}

fun spinnerCategoryFilter(context: Context, arrayList: ArrayList<CategoryIdName>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, arrayList
    )

    spinner.adapter = adapter
}

fun spinnerCategoryChange(context: Context, arrayList: ArrayList<CategoryItem>, spinner: Spinner) {
    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, arrayList
    )

    spinner.adapter = adapter
}


fun spinnerTransaction(context: Context, spinner: Spinner) {
    val transactionList: ArrayList<TransactionType> = ArrayList()

    transactionList.add(TransactionType(-1, "Операция"))
    transactionList.add(TransactionType(0, "Доход"))
    transactionList.add(TransactionType(1, "Расход"))
    transactionList.add(TransactionType(2, "Перевод"))

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, transactionList
    )

    spinner.adapter = adapter

}

fun spinnerPeriodFilter(context: Context, spinner: Spinner) {
    val period: ArrayList<Period> = ArrayList()

    period.add(Period("", "Период"))
    period.add(Period(getWeek(), "Неделя"))
    period.add(Period(getMonth(), "Месяц"))
    period.add(Period(getYear(), "Год"))
    period.add(Period("period", "За период"))

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, period
    )

    spinner.adapter = adapter

}

fun spinnerPeriodAnalytics(context: Context, spinner: Spinner) {
    val period: ArrayList<Period> = ArrayList()

    period.add(Period(getWeek(), "Неделя"))
    period.add(Period(getMonth(), "Месяц"))
    period.add(Period(getYear(), "Год"))

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, period
    )

    spinner.adapter = adapter

}

fun spinnerTransactionAnalytics(context: Context, spinner: Spinner) {
    val transactionList: ArrayList<TransactionType> = ArrayList()

    transactionList.add(TransactionType(0, "Доход"))
    transactionList.add(TransactionType(1, "Расход"))

    val adapter = ArrayAdapter(
        context,
        R.layout.spinner_filter, transactionList
    )

    spinner.adapter = adapter

}

fun date(text: TextView) : DatePickerDialog.OnDateSetListener {
    return DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        text.text = sdf.format(calendar.time)
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

@SuppressLint("SimpleDateFormat")
@Throws(ParseException::class)
fun formatDate(dateInput: String?): String? {
    val originalFormat = SimpleDateFormat("dd.MM.yyyy")
    val targetFormat = SimpleDateFormat("yyyy-MM-dd")
    val date: Date? = originalFormat.parse(dateInput)
    return targetFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
@Throws(ParseException::class)
fun formatDateAdapters(dateInput: String?): String? {
    val originalFormat = SimpleDateFormat("yyyy-MM-dd")
    val targetFormat = SimpleDateFormat("dd.MM.yyyy")
    val date: Date? = originalFormat.parse(dateInput)
    return targetFormat.format(date)
}


