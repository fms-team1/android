package com.example.neofin.adapters

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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.itemView.apply {
            if (current.transactionType == "INCOME") {
                holder.itemView.imageJournal.setImageResource(R.drawable.ic_income)
            } else {
                holder.itemView.imageJournal.setImageResource(R.drawable.ic_expense)
            }

            holder.itemView.nameJournal.text = current.category
            holder.itemView.dateJournal.text = current.createdDate
            holder.itemView.sumJournal.text = current.amount.toString()

            holder.itemView.nameJournal.setOnClickListener {
                onItemClickListener?.let { it(current) }
            }
        }
    }

    fun setOnItemClickListener(listener: (JournalItem) -> Unit) {
        onItemClickListener = listener
    }
}