package com.example.neofin.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.neofin.R
import com.example.neofin.retrofit.data.filteredJournal.FilteredJournalItem
import com.example.neofin.utils.formatDate
import com.example.neofin.utils.formatDateAdapters
import kotlinx.android.synthetic.main.filtered_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class FilteredJournalAdapter : RecyclerView.Adapter<FilteredJournalAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<FilteredJournalItem>() {
        override fun areItemsTheSame(oldItem: FilteredJournalItem, newItem: FilteredJournalItem): Boolean {
            return oldItem.categoryName == newItem.categoryName
        }

        override fun areContentsTheSame(oldItem: FilteredJournalItem, newItem: FilteredJournalItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.filtered_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((FilteredJournalItem) -> Unit)? = null

    @SuppressLint("SetTextI18n", "ResourceAsColor", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.itemView.apply {
            holder.itemView.nameFiltered.text = current.categoryName
            holder.itemView.dateFiltered.text = formatDateAdapters(current.createdDate.substringBefore('T'))
            holder.itemView.nameSurnameUser.text = "${current.accountantName} ${current.accountantSurname}"
            if (current.counterpartySurname == null){
                holder.itemView.nameSurnameAgent.text = current.counterpartyName
            } else {
                holder.itemView.nameSurnameAgent.text = "${current.counterpartyName} ${current.counterpartySurname}"
            }
            holder.itemView.walletFiltered.text = current.walletName
            holder.itemView.commentFiltered.text = current.comment

            when (current.neoSection) {
                "NEOBIS" -> holder.itemView.sectionFiltered.text = "Neobis"
                "NEOLABS" -> holder.itemView.sectionFiltered.text = "Neolabs"
            }

            when (current.transactionType) {
                "INCOME" -> {
                    holder.itemView.imageFiltered.setImageResource(R.drawable.ic_income2)
                    holder.itemView.sumFiltered.setTextColor(Color.parseColor("#4AAF39"))
                    holder.itemView.sumFiltered.text = "+ ${current.amount} с"
                    walletNotTransfer.visibility = View.VISIBLE
                    sectionLayout.visibility = View.VISIBLE
                    agentLayout.visibility = View.VISIBLE
                }
                "EXPENSE" -> {
                    holder.itemView.imageFiltered.setImageResource(R.drawable.ic_expense2)
                    holder.itemView.sumFiltered.setTextColor(Color.parseColor("#E11616"))
                    holder.itemView.sumFiltered.text = "- ${current.amount} с"
                    walletNotTransfer.visibility = View.VISIBLE
                    sectionLayout.visibility = View.VISIBLE
                    agentLayout.visibility = View.VISIBLE
                }
                else -> {
                    holder.itemView.imageFiltered.setImageResource(R.drawable.ic_transfer2)
                    holder.itemView.sumFiltered.setTextColor(Color.parseColor("#3B3BB7"))
                    holder.itemView.sumFiltered.text = "${current.amount} с"
                    holder.itemView.fromWallet.text = current.walletName
                    holder.itemView.toWallet.text = current.transferWalletName
                    transferWallets.visibility = View.VISIBLE
                    walletNotTransfer.visibility = View.GONE
                    sectionLayout.visibility = View.GONE
                    agentLayout.visibility = View.GONE
                }
            }

            holder.itemView.setOnClickListener {
                if (bigInfo.visibility == View.GONE) {
                    bigInfo.visibility = View.VISIBLE
                } else {
                    bigInfo.visibility = View.GONE
                }
            }

            holder.itemView.change_filtered_button.setOnClickListener {
                onItemClickListener?.let { it(current) }
            }
        }
    }

    fun setOnItemClickListener(listener: (FilteredJournalItem) -> Unit) {
        onItemClickListener = listener
    }
}