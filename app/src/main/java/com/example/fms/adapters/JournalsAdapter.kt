package com.example.fms.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fms.R
import com.example.fms.retrofit.data.journal.JournalItem
import kotlinx.android.synthetic.main.journals.view.*

class JournalsAdapter : RecyclerView.Adapter<JournalsAdapter.MyViewHolder>(){

    private var myList = emptyList<JournalItem>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.journals, parent, false))
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    private var onItemClickListener: ((JournalItem) -> Unit)? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = myList[position]
        val view = holder.itemView

        if (current.transactionType == "INCOME") {
            view.imageJournal.setImageResource(R.drawable.ic_income)
        } else {
            view.imageJournal.setImageResource(R.drawable.ic_expense)
        }

        view.nameJournal.text = current.category
        view.dateJournal.text = current.createdDate
        view.sumJournal.text = current.amount.toString()

        setOnItemClickListener {
            onItemClickListener?.let {
                it(current)
            }
        }
    }

    fun setData(newList: List<JournalItem>){
        myList = newList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (JournalItem) -> Unit) {
        onItemClickListener = listener
    }
}