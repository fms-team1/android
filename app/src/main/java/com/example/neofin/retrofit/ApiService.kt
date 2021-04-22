package com.example.neofin.retrofit

import com.example.neofin.retrofit.data.addCategory.AddCategory
import com.example.neofin.retrofit.data.addGroup.GroupAdd
import com.example.neofin.retrofit.data.addNewAccount.AddNewUser
import com.example.neofin.retrofit.data.addWallets.AddWallet
import com.example.neofin.retrofit.data.allGroups.Groups
import com.example.neofin.retrofit.data.allUsers.AllUsers
import com.example.neofin.retrofit.data.analytics.Analytics
import com.example.neofin.retrofit.data.archiveCategory.ArchiveCategory
import com.example.neofin.retrofit.data.archiveGroups.ArchiveGroup
import com.example.neofin.retrofit.data.archiveWallet.ArchiveWallet
import com.example.neofin.retrofit.data.category.Category
import com.example.neofin.retrofit.data.changeJournal.ChangeJournal
import com.example.neofin.retrofit.data.changePassword.ChangePassword
import com.example.neofin.retrofit.data.filteredJournal.FilteredJournalItem
import com.example.neofin.retrofit.data.getAllAgents.AllAgents
import com.example.neofin.retrofit.data.journal.AllJournalItem
import com.example.neofin.retrofit.data.journal.JournalItem
import com.example.neofin.retrofit.data.journalById.JournalById
import com.example.neofin.retrofit.data.loginRequest.LoginRequest
import com.example.neofin.retrofit.data.tokenResponse.TokenResponse
import com.example.neofin.retrofit.data.transaction.Transaction
import com.example.neofin.retrofit.data.transactionAdding.AddTransactionOrExpense
import com.example.neofin.retrofit.data.transactionAdding.AddTransfer
import com.example.neofin.retrofit.data.updateUser.UpdateUser
import com.example.neofin.retrofit.data.user.CurrentUser
import com.example.neofin.retrofit.data.wallet.GetWallet
import com.example.neofin.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
//    @Headers("Content-Type: application/json")
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<TokenResponse>

//    @Headers("Content-Type: application/json")
    @POST(Constants.ADD_INCOME_EXPENSE)
    fun addIncomeOrExpense(@Header("Authorization") token: String, @Body request: AddTransactionOrExpense): Call<Void>

//    @Headers("Content-Type: application/json")
    @POST(Constants.ADD_WALLET)
    fun addWallet(@Header("Authorization") token: String, @Body request: AddWallet): Call<Void>

//    @Headers("Content-Type: application/json")
    @POST(Constants.ADD_CATEGORY)
    fun addCategory(@Header("Authorization") token: String, @Body request: AddCategory): Call<Void>

//    @Headers("Content-Type: application/json")
    @PUT(Constants.ARCHIVE_WALLET)
    fun archiveWallet(@Header("Authorization") token: String, @Body request: ArchiveWallet): Call<Void>

//    @Headers("Content-Type: application/json")
    @PUT(Constants.ARCHIVE_CATEGORY)
    fun archiveCategory(@Header("Authorization") token: String, @Body request: ArchiveCategory): Call<Void>

//    @Headers("Content-Type: application/json")
    @PUT(Constants.ARCHIVE_GROUPS)
    fun archiveGroup(@Header("Authorization") token: String, @Body request: ArchiveGroup): Call<Void>

//    @Headers("Content-Type: application/json")
    @GET(Constants.GET_ALL_CATEGORY)
    fun getAllCategory(@Header("Authorization") token: String): Call<List<ArchiveCategory>>

//    @Headers("Content-Type: application/json")
    @GET(Constants.GET_ALL_GROUPS_ARCHIVE)
    fun getAllGroupsArchive(@Header("Authorization") token: String): Call<List<ArchiveGroup>>

//    @Headers("Content-Type: application/json")
    @POST(Constants.ADD_GROUP)
    fun addGroup(@Header("Authorization") token: String, @Body request: GroupAdd): Call<Void>

//    @Headers("Content-Type: application/json")
    @POST(Constants.ADD_TRANSFER)
    fun addTransfer(@Header("Authorization") token: String, @Body request: AddTransfer): Call<Void>

//    @Headers("Content-Type: application/json")
    @GET(Constants.HOME_URL)
    fun getHomePage(@Header("Authorization") token: String): Call<Transaction>

//    @Headers("Content-Type: application/json")
    @GET(Constants.HOME_URL_BY_PERIOD)
    fun getHomePageByPeriod(@Header("Authorization") token: String,
                            @Path("period") period : String): Call<Transaction>

//    @Headers("Content-Type: application/json")
    @GET(Constants.JOURNAL_URL)
    fun getJournal(@Header("Authorization") token: String): Call<MutableList<AllJournalItem>>

//    @Headers("Content-Type: application/json")
    @GET(Constants.JOURNAL_SINGLE_URL)
    fun getJournalById(@Header("Authorization") token: String, @Path("id") id: Int): Call<JournalById>

//    @Headers("Content-Type: application/json")
    @GET(Constants.GET_JOURNAL_BY_SECTION)
    fun getJournalBySection(@Header("Authorization") token: String, @Query("neoSection") section: String): Call<MutableList<JournalItem>>

//    @Headers("Content-Type: application/json")
    @GET(Constants.GET_CURRENT_USER)
    fun getCurrentUser(@Header("Authorization") token: String): Call<CurrentUser>

//    @Headers("Content-Type: application/json")
    @GET(Constants.GET_ALL_USERS)
    fun getAllUsers(@Header("Authorization") token: String): Call<AllUsers>

//    @Headers("Content-Type: application/json")
    @GET(Constants.GET_WALLETS)
    fun getWallets(@Header("Authorization") token: String): Call<GetWallet>

//    @Headers("Content-Type: application/json")
    @GET(Constants.GET_ALL_WALLETS)
    fun getAllWallets(@Header("Authorization") token: String): Call<List<ArchiveWallet>>

//    @Headers("Content-Type: application/json")
    @GET(Constants.GET_ALL_AGENTS)
    fun getAllAgents(@Header("Authorization") token: String): Call<AllAgents>

//    @Headers("Content-Type: application/json")
    @GET(Constants.GET_ALL_GROUPS)
    fun getAllGroups(@Header("Authorization") token: String): Call<List<Groups>>

//    @Headers("Content-Type: application/json")
    @GET(Constants.CATEGORY_ALL)
    fun getCategory(
        @Header("Authorization") token: String,
        @Query("neoSectionId") section: Int,
        @Query("transactionTypeId") type: Int): Call<Category>

//    @Headers("Content-Type: application/json")
    @GET(Constants.GET_FILTERED)
    fun getFiltered(
        @Header("Authorization") token: String,
        @Query("categoryId") categoryId: Int?,
        @Query("counterpartyId") counterpartyId: Int?,
        @Query("endDate") endDate: String?,
        @Query("neoSectionId") neoSectionId: Int?,
        @Query("startDate") startDate: String?,
        @Query("transactionTypeId") transactionType: Int?,
        @Query("transferWalletId") transferWalletId: Int?,
        @Query("userId") userId: Int?,
        @Query("walletId") walletId: Int?) : Call<MutableList<FilteredJournalItem>>

//    @Headers("Content-Type: application/json")
    @GET(Constants.GET_ANALYTICS)
    fun getAnalytics(
        @Header("Authorization") token: String,
        @Query("endDate") endDate: String,
        @Query("neoSectionId") neoSectionId: Int,
        @Query("startDate") startDate: String,
        @Query("transactionTypeId") transactionType: Int) : Call<Analytics>

//    @Headers("Content-Type: application/json")
    @PUT(Constants.CHANGE_PASSWORD)
    fun changePassword(@Header("Authorization") token: String,
                       @Body request : ChangePassword) : Call<Void>

//    @Headers("Content-Type: application/json")
    @PUT(Constants.UPDATE_JOURNAL_ITEM)
    fun changeJournalItem(@Header("Authorization") token: String,
                       @Body request : ChangeJournal) : Call<Void>

    @PUT(Constants.UPDATE_USER)
    fun changeUser(@Header("Authorization") token: String,
                          @Body request : UpdateUser) : Call<Void>


//    @Headers("Content-Type: application/json")
    @POST(Constants.ADD_USER)
    fun addUser(@Header("Authorization") token: String, @Body request: AddNewUser): Call<Void>
}