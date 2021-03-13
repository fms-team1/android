package com.example.neofin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neofin.R
import com.example.neofin.retrofit.data.transaction.WalletBalance
import kotlinx.android.synthetic.main.wallets.view.*

class WalletAdapter: RecyclerView.Adapter<WalletAdapter.MyViewHolder>() {
    private var myList = emptyList<WalletBalance>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.wallets, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = myList[position]

        when (current.walletName) {
            "Наличные" -> {
                holder.itemView.logoWallet.setImageResource(R.drawable.money)
            }
            "Demir Bank" -> {
                holder.itemView.logoWallet.setImageResource(R.drawable.demir)
            }
            "О деньги" -> {
                holder.itemView.logoWallet.setImageResource(R.drawable.o)
            }
            "Elsom" -> {
                holder.itemView.logoWallet.setImageResource(R.drawable.elsom)
            }
            else -> {
                holder.itemView.logoWallet.setImageResource(R.drawable.ic_money)
            }
        }

        holder.itemView.walletName.text = current.walletName
        holder.itemView.walletBalance.text = "${current.balance} с"
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    fun setData(newList: List<WalletBalance>) {
        this.myList = newList
        notifyDataSetChanged()
    }
}