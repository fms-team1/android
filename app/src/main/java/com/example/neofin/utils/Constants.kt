package com.example.neofin.utils

object Constants {
    const val BASE_URL = "https://neo-fms.herokuapp.com"
    const val LOGIN_URL = "/login"
    const val HOME_URL = "/home"
    const val HOME_URL_BY_PERIOD = "/home/{period}"
    const val JOURNAL_URL = "/journal/getAll"
    const val JOURNAL_SINGLE_URL = "/journal/getById/{id}"
    const val ADD_INCOME_EXPENSE = "/transaction/addIncomeOrExpense"
    const val ADD_TRANSFER = "/transaction/addTransfer"
    const val CATEGORY_ALL = "/category/getAllActiveCategoriesBySectionAndType"
    const val GET_WALLETS = "/wallet/getAllActiveWallets"
    const val GET_CURRENT_USER = "/user/getCurrentUser"
    const val GET_JOURNAL_BY_SECTION = "/journal/getByNeoSection"
    const val CHANGE_PASSWORD ="/user/changePassword"
    const val ADD_USER = "/registration/newAccountant"
}