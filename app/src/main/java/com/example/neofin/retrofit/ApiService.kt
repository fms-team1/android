package com.example.neofin.retrofit

import com.example.neofin.retrofit.data.addingResponse.AddResponse
import com.example.neofin.retrofit.data.category.Category
import com.example.neofin.retrofit.data.currentUser.CurrentUser
import com.example.neofin.retrofit.data.journal.AllJournalItem
import com.example.neofin.retrofit.data.journal.JournalItem
import com.example.neofin.retrofit.data.journalById.JournalById
import com.example.neofin.retrofit.data.loginRequest.LoginRequest
import com.example.neofin.retrofit.data.tokenResponse.TokenResponse
import com.example.neofin.retrofit.data.transaction.Transaction
import com.example.neofin.retrofit.data.transactionAdding.AddTransactionOrExpense
import com.example.neofin.retrofit.data.transactionAdding.AddTransfer
import com.example.neofin.retrofit.data.wallet.GetWallet
import com.example.neofin.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<TokenResponse>

    @Headers("Content-Type: application/json")
    @POST(Constants.ADD_INCOME_EXPENSE)
    fun addIncomeOrExpense(@Header("Authorization") token: String, @Body request: AddTransactionOrExpense): Call<AddResponse>

    @Headers("Content-Type: application/json")
    @POST(Constants.ADD_TRANSFER)
    fun addTransfer(@Header("Authorization") token: String, @Body request: AddTransfer): Call<String>

    @Headers("Content-Type: application/json")
    @GET(Constants.HOME_URL)
    fun getHomePage(@Header("Authorization") token: String): Call<Transaction>

    @Headers("Content-Type: application/json")
    @GET(Constants.HOME_URL_BY_PERIOD)
    fun getHomePageByPeriod(@Header("Authorization") token: String,
                            @Path("period") period : String): Call<Transaction>

    @Headers("Content-Type: application/json")
    @GET(Constants.JOURNAL_URL)
    fun getJournal(@Header("Authorization") token: String): Call<MutableList<AllJournalItem>>

    @Headers("Content-Type: application/json")
    @GET(Constants.JOURNAL_SINGLE_URL)
    fun getJournalById(@Header("Authorization") token: String, @Path("id") id: Int): Call<JournalById>

    @Headers("Content-Type: application/json")
    @GET(Constants.GET_JOURNAL_BY_SECTION)
    fun getJournalBySection(@Header("Authorization") token: String, @Query("neoSection") section: String): Call<MutableList<JournalItem>>

    @Headers("Content-Type: application/json")
    @GET(Constants.GET_CURRENT_USER)
    fun getCurrentUser(@Header("Authorization") token: String): Call<CurrentUser>

    @Headers("Content-Type: application/json")
    @GET(Constants.GET_WALLETS)
    fun getWallets(@Header("Authorization") token: String): Call<GetWallet>

    @Headers("Content-Type: application/json")
    @GET(Constants.CATEGORY_ALL)
    fun getCategory(
        @Header("Authorization") token: String,
        @Query("neoSection") section: String,
        @Query("transactionType") type: String): Call<Category>
}