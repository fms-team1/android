package com.example.neofin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.neofin.R
import com.example.neofin.retrofit.data.archiveWallet.ArchiveWallet
import com.example.neofin.retrofit.data.wallet.GetWalletItem
import kotlinx.android.synthetic.main.wallet_archive.view.*

class ArchiveWalletAdapter :  RecyclerView.Adapter<ArchiveWalletAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<ArchiveWallet>() {
        override fun areItemsTheSame(oldItem: ArchiveWallet, newItem: ArchiveWallet): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ArchiveWallet, newItem: ArchiveWallet): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.wallet_archive,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((ArchiveWallet) -> Unit)? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.itemView.apply {
            holder.itemView.nameWallet.text = current.name
            if (current.status == "ACCESSIBLE") {
                holder.itemView.balanceWallet.text = "Доступен"
            } else {
                holder.itemView.balanceWallet.text = "Архивирован"
            }


            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(current) }
            }
        }
    }

    fun setOnItemClickListener(listener: (ArchiveWallet) -> Unit) {
        onItemClickListener = listener
    }
}