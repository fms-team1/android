package com.example.neofin.ui.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.ui.LoginActivity
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import androidx.navigation.fragment.findNavController
import com.example.neofin.retrofit.data.addCategory.AddCategory
import com.example.neofin.retrofit.data.addWallets.AddWallet
import com.example.neofin.retrofit.data.user.CurrentUser
import com.example.neofin.ui.user.data.Section
import com.example.neofin.ui.user.data.Type
import com.example.neofin.utils.*
import kotlinx.android.synthetic.main.dialog_add_category.*
import kotlinx.android.synthetic.main.dialog_add_category.view.*
import kotlinx.android.synthetic.main.dialog_add_wallet.*
import kotlinx.android.synthetic.main.dialog_add_wallet.view.*
import retrofit2.Callback


class UserFragment : Fragment(R.layout.fragment_user) {
    var section = ""
    var type = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()
        getCurrentUser()

        logout?.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                val preferences: SharedPreferences? =
                    requireActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE)

                preferences?.edit()?.remove("remember")?.apply();
                preferences?.edit()?.remove("password")?.apply();

                preferences?.edit()?.remove("email")?.apply()

            }

        }
        back_user?.setOnClickListener {
            findNavController().navigate(R.id.navigation_home)
        }

        change_textView?.setOnClickListener {
            findNavController().navigate(R.id.updatePasswordFragment)
        }
        addUser?.setOnClickListener {
            findNavController().navigate(R.id.addNewUserFragment)
        }

        archive?.setOnClickListener {
            findNavController().navigate(R.id.archiveFragment)
        }

        addWallet?.setOnClickListener {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_wallet,null)
            val mBuilder = context?.let { it1 ->
                AlertDialog.Builder(it1)
                    .setView(mDialogView)
            }
            val  mAlertDialog = mBuilder?.show()
            mDialogView?.cancel_wallet?.setOnClickListener {
                mAlertDialog?.dismiss()
            }

            mDialogView?.create_wallet?.setOnClickListener {
                val balance = mDialogView.et_add_balance.text.toString().trim()
                if (balance.isNotEmpty()){
                    addWallet(balance.toInt(), mDialogView.et_add_name.text.toString())
                } else {
                    toast(requireContext(), "Введите баланс!")
                }
                mAlertDialog?.dismiss()
            }
        }

        addCategory?.setOnClickListener {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_category,null)
            val mBuilder = context?.let { it1 ->
                AlertDialog.Builder(it1)
                    .setView(mDialogView)
            }
            val  mAlertDialog = mBuilder?.show()
            setupCategorySpinners(mDialogView.sectionAdd, mDialogView.typeAdd, requireContext())
            mDialogView?.cancel_category?.setOnClickListener {
                mAlertDialog?.dismiss()
            }

            mDialogView?.create_category?.setOnClickListener {
                val name = mDialogView.et_add_category_name.text.toString()
                addCategory(name, section, type)
                mAlertDialog?.dismiss()
            }
        }


    }


    private fun getCurrentUser() = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getCurrentUser(token).enqueue(object : Callback<CurrentUser> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<CurrentUser>, response: Response<CurrentUser>) {
                val name = response.body()?.name
                val surname = response.body()?.surname
                if (response.body()?.role?.id != 1) {
                    addWalletLayout?.visibility = View.GONE
                    addCategoryLayout?.visibility = View.GONE
                    archiveLayout?.visibility = View.GONE
                } else {
                    addWalletLayout?.visibility = View.VISIBLE
                    addCategoryLayout?.visibility = View.VISIBLE
                    archiveLayout?.visibility = View.VISIBLE
                }
                profile?.text = "$name $surname"
                email?.text = response.body()?.email
                response.body()?.groups?.forEach {
                    groups?.text = it.name
                }
                phoneProfile?.text = response.body()?.phoneNumber
            }

            override fun onFailure(call: Call<CurrentUser>, t: Throwable) {

            }

        })

    }

    private fun setupCategorySpinners(spinnerSection: Spinner,
                                      spinnerType: Spinner, context:
                                      Context) = CoroutineScope(Dispatchers.IO).launch {
        spinnerSectionUser(context, spinnerSection)
        spinnerTypeUser(context, spinnerType)
        spinnerSection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val sectionName: Section = parent.selectedItem as Section
                section = sectionName.backName

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val typeName: Type = parent.selectedItem as Type
                type = typeName.backName
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun addWallet(balance: Int, name: String) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val walletBody = AddWallet(balance, name)
        retIn.addWallet(token, walletBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when {
                    response.code() == 404 -> {
                        snackbar(requireView(), "Такой кошелек уже существует!", Color.parseColor("#E11616"))
                    }
                    response.code() == 200 -> {
                        snackbar(
                            requireView(),
                            "Кошелек добавлен!",
                            Color.parseColor("#4AAF39")
                        )
                    }
                    else -> {
                        snackbar(requireView(), "Неизвестная ошибка!", Color.parseColor("#E11616"))
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                logs("Error in UserFr, addWallet")
            }

        })
    }

    private fun addCategory(name: String, section: String, type: String) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val categoryBody = AddCategory(name, section, type)
        retIn.addCategory(token, categoryBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when {
                    response.code() == 404 -> {
                        snackbar(requireView(), "Такая категория уже существует!", Color.parseColor("#E11616"))
                    }
                    response.code() == 200 -> {
                        snackbar(
                            requireView(),
                            "Категория добавлена!",
                            Color.parseColor("#4AAF39")
                        )
                    }
                    else -> {
                        snackbar(requireView(), "Неизвестная ошибка!", Color.parseColor("#E11616"))
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                logs("Error in UserFr, addCategory")
            }

        })
    }
}