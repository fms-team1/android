package com.example.neofin.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.neofin.retrofit.data.journal.JournalItem
import com.example.neofin.R
import com.example.neofin.utils.formatDate
import com.example.neofin.utils.formatDateAdapters
import kotlinx.android.synthetic.main.journals.view.*
import java.text.SimpleDateFormat
import java.util.*

class JournalAdapter : RecyclerView.Adapter<JournalAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<JournalItem>() {
        override fun areItemsTheSame(oldItem: JournalItem, newItem: JournalItem): Boolean {
            return oldItem.categoryName == newItem.categoryName
        }

        override fun areContentsTheSame(oldItem: JournalItem, newItem: JournalItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.journals,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((JournalItem) -> Unit)? = null

    @SuppressLint("SetTextI18n", "ResourceAsColor", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.itemView.apply {
            holder.itemView.nameJournal.text = current.categoryName
            holder.itemView.dateJournal.text = formatDateAdapters(current.createdDate.substringBefore('T'))

            when (current.transactionType) {
                "INCOME" -> {
                    holder.itemView.imageJournal.setImageResource(R.drawable.ic_income2)
                    holder.itemView.sumJournal.setTextColor(Color.parseColor("#4AAF39"))
                    holder.itemView.sumJournal.text = "+ ${current.amount} с"
                }
                "EXPENSE" -> {
                    holder.itemView.imageJournal.setImageResource(R.drawable.ic_expense2)
                    holder.itemView.sumJournal.setTextColor(Color.parseColor("#E11616"))
                    holder.itemView.sumJournal.text = "- ${current.amount} с"
                }
                else -> {
                    holder.itemView.imageJournal.setImageResource(R.drawable.ic_transfer2)
                    holder.itemView.sumJournal.setTextColor(Color.parseColor("#3B3BB7"))
                    holder.itemView.sumJournal.text = "${current.amount} с"
                }
            }

            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(current) }
            }
        }
    }

    fun setOnItemClickListener(listener: (JournalItem) -> Unit) {
        onItemClickListener = listener
    }
}