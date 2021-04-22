package com.example.neofin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.neofin.R
import com.example.neofin.retrofit.data.allUsers.AllUsersItem
import com.example.neofin.retrofit.data.archiveCategory.ArchiveCategory
import kotlinx.android.synthetic.main.category_archive.view.*
import kotlinx.android.synthetic.main.category_archive.view.nameCategory
import kotlinx.android.synthetic.main.user_archive.view.*

class ArchiveUserAdapter :  RecyclerView.Adapter<ArchiveUserAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<AllUsersItem>() {
        override fun areItemsTheSame(oldItem: AllUsersItem, newItem: AllUsersItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: AllUsersItem, newItem: AllUsersItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.user_archive,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((AllUsersItem) -> Unit)? = null

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.itemView.apply {
            holder.itemView.nameSurname.text = "${current.name} ${current.surname}"
            when (current.userStatus) {
                "APPROVED" -> {
                    holder.itemView.statusUser.text = "Доступен"
                }
                "NEW" -> {
                    holder.itemView.statusUser.text = "Новый пользователь"
                }
                "BLOCKED" -> {
                    holder.itemView.statusUser.text = "Заблокирован"
                }
                else -> {
                    holder.itemView.statusUser.text = "Архивирован"
                }
            }


            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(current) }
            }
        }
    }

    fun setOnItemClickListener(listener: (AllUsersItem) -> Unit) {
        onItemClickListener = listener
    }
}