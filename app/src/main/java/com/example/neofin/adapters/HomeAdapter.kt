package com.example.neofin.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neofin.R
import com.example.neofin.retrofit.data.transaction.LastFifteenTransaction
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
            when (current.transactionType) {
                "INCOME" -> {
                    holder.itemView.imageTransaction.setImageResource(R.drawable.ic_income2)
                    holder.itemView.sumTransaction.setTextColor(Color.parseColor("#4AAF39"))
                    holder.itemView.sumTransaction.text = "+ ${current.amount} с"
                }
                "EXPENSE" -> {
                    holder.itemView.imageTransaction.setImageResource(R.drawable.ic_expense2)
                    holder.itemView.sumTransaction.setTextColor(Color.parseColor("#E11616"))
                    holder.itemView.sumTransaction.text = "- ${current.amount} с"
                }
                else -> {
                    holder.itemView.imageTransaction.setImageResource(R.drawable.ic_transfer2)
                    holder.itemView.sumTransaction.setTextColor(Color.parseColor("#3B3BB7"))
                    holder.itemView.sumTransaction.text = "${current.amount} с"
                }
            }

            holder.itemView.nameTransaction.text = current.categoryName
            holder.itemView.dateTransaction.text = current.createdDate.substringBefore('T')

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