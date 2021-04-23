package com.example.neofin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.neofin.R
import com.example.neofin.retrofit.data.archiveCategory.ArchiveCategory
import kotlinx.android.synthetic.main.category_archive.view.*
import kotlinx.android.synthetic.main.journals.view.*

class ArchiveCategoryAdapter :  RecyclerView.Adapter<ArchiveCategoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<ArchiveCategory>() {
        override fun areItemsTheSame(oldItem: ArchiveCategory, newItem: ArchiveCategory): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ArchiveCategory, newItem: ArchiveCategory): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.category_archive,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((ArchiveCategory) -> Unit)? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.itemView.apply {
            holder.itemView.nameCategory.text = current.name
            if (current.categoryStatus == "ACTIVE") {
                holder.itemView.statusCategory.text = "Доступен"
            } else {
                holder.itemView.statusCategory.text = "Архивирован"
            }


            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(current) }
            }
        }
    }

    fun setOnItemClickListener(listener: (ArchiveCategory) -> Unit) {
        onItemClickListener = listener
    }
}