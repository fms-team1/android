package com.example.neofin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.neofin.R
import com.example.neofin.retrofit.data.archiveGroups.ArchiveGroup
import kotlinx.android.synthetic.main.group_archive.view.*
import kotlinx.android.synthetic.main.journals.view.*

class ArchiveGroupAdapter :  RecyclerView.Adapter<ArchiveGroupAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<ArchiveGroup>() {
        override fun areItemsTheSame(oldItem: ArchiveGroup, newItem: ArchiveGroup): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ArchiveGroup, newItem: ArchiveGroup): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.group_archive,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((ArchiveGroup) -> Unit)? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.itemView.apply {
            holder.itemView.nameGroup.text = current.name
            if (current.groupStatus == "ACTIVE") {
                holder.itemView.statusGroup.text = "Доступен"
            } else {
                holder.itemView.statusGroup.text = "Архивирован"
            }


            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(current) }
            }
        }
    }

    fun setOnItemClickListener(listener: (ArchiveGroup) -> Unit) {
        onItemClickListener = listener
    }
}