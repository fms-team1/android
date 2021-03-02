package com.example.fms.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fms.R
import com.example.fms.retrofit.data.transactios.LastFifteenTransaction
import kotlinx.android.synthetic.main.transactions.view.*

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    private var myList = emptyList<LastFifteenTransaction>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.transactions, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = myList[position]

        holder.itemView.createdData.text = current.createdDate
        holder.itemView.amount.text = current.amount.toString()
        holder.itemView.comment.text = current.comment
        holder.itemView.user.text = current.name
        holder.itemView.person.text = current.lastName
        holder.itemView.deletedDate.text = current.deletedDate?.toString()

        val availableBalance = current.wallet.availableBalance
        val createdDate = current.wallet.createdDate
        val deletedDate = current.wallet.deletedDate
        val id = current.wallet.id
        val wallet = current.wallet.wallet
        val walletStatus = current.wallet.walletStatus
        

        holder.itemView.wallet.text = "$availableBalance\n$createdDate\n$deletedDate\n$wallet"
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    fun setData(newList: List<LastFifteenTransaction>) {
        this.myList = newList
        notifyDataSetChanged()
    }
}