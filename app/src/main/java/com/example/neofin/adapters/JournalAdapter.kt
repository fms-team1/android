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
import kotlinx.android.synthetic.main.journals.view.*

class JournalAdapter : RecyclerView.Adapter<JournalAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<JournalItem>() {
        override fun areItemsTheSame(oldItem: JournalItem, newItem: JournalItem): Boolean {
            return oldItem.category == newItem.category
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

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.itemView.apply {
            holder.itemView.sumJournal.text = "${current.amount} с"
            holder.itemView.nameJournal.text = current.category
            holder.itemView.dateJournal.text = current.createdDate.substringBefore('T')

            when (current.transactionType) {
                "INCOME" -> {
                    holder.itemView.imageJournal.setImageResource(R.drawable.ic_income2)
                    holder.itemView.sumJournal.setTextColor(Color.parseColor("#4AAF39"))
                }
                "EXPENSE" -> {
                    holder.itemView.imageJournal.setImageResource(R.drawable.ic_expense2)
                    holder.itemView.sumJournal.setTextColor(Color.parseColor("#E11616"))
                }
                else -> {
                    holder.itemView.imageJournal.setImageResource(R.drawable.ic_transfer2)
                    holder.itemView.sumJournal.setTextColor(Color.parseColor("#3B3BB7"))
                }
            }

            holder.itemView.nameJournal.setOnClickListener {
                onItemClickListener?.let { it(current) }
            }
        }
    }

    fun setOnItemClickListener(listener: (JournalItem) -> Unit) {
        onItemClickListener = listener
    }
}