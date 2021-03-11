package com.example.neofin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neofin.retrofit.data.transaction.LastFifteenTransaction
import com.example.neofin.R
import com.example.neofin.utils.logs
import kotlinx.android.synthetic.main.transactions.view.*

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    private var myList = emptyList<LastFifteenTransaction>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.transactions, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = myList[position]

        val date = current.createdDate

        val nameAccount = current.accountantName
        val surnameAccount = current.accountantName

        val nameAgent = current.counterpartyName
        val surnameAgent = current.counterpartySurname
        try {
            if (current.transactionType == "INCOME") {
                holder.itemView.imageTransaction.setImageResource(R.drawable.ic_income)
            } else {
                holder.itemView.imageTransaction.setImageResource(R.drawable.ic_expense)
            }

            holder.itemView.nameTransaction.text = current.categoryName
            holder.itemView.dateTransaction.text = current.createdDate
            holder.itemView.sumTransaction.text = current.amount.toString()

            holder.itemView.comment.text = current.comment
            holder.itemView.accountNameSurname.text = "$nameAccount $surnameAccount"
            holder.itemView.agentNameSurname.text = "$nameAgent $surnameAgent"

            holder.itemView.neoSection.text = current.neoSection

            holder.itemView.walletName.text = current.walletName

            holder.itemView.nameTransaction.setOnClickListener {
                if (holder.itemView.additionalInfo.visibility == View.GONE) {
                    holder.itemView.additionalInfo.visibility = View.VISIBLE
                } else {
                    holder.itemView.additionalInfo.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            logs("Error")
        }
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    fun setData(newList: List<LastFifteenTransaction>) {
        this.myList = newList
        notifyDataSetChanged()
    }
}