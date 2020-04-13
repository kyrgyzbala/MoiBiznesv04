package com.kyrgyzcoder.moibiznesv04.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kyrgyzcoder.moibiznesv04.*
import com.kyrgyzcoder.moibiznesv04.dataItemsSold.ItemSold
import com.kyrgyzcoder.moibiznesv04.utils.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SoldItemsRecyclerViewAdapter :
    ListAdapter<ItemSold, SoldItemsRecyclerViewAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemSold>() {
            override fun areItemsTheSame(oldItem: ItemSold, newItem: ItemSold): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ItemSold, newItem: ItemSold): Boolean {
                return (oldItem.itemName == newItem.itemName &&
                        oldItem.itemPrice == newItem.itemPrice &&
                        oldItem.itemQuantity == newItem.itemQuantity &&
                        oldItem.itemCategory == newItem.itemCategory &&
                        oldItem.itemColor == newItem.itemColor)
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemName: TextView = itemView.findViewById(R.id.textViewItemName)
        val itemQuantity: TextView = itemView.findViewById(R.id.textViewItemQuantity)
        val itemPrice: TextView = itemView.findViewById(R.id.textViewItemPrice)
        val itemCategory: TextView = itemView.findViewById(R.id.textViewItemCategory)
        val itemTotal: TextView = itemView.findViewById(R.id.textViewItemTotalPrice)
        val itemCreatedDate: TextView = itemView.findViewById(R.id.textViewItemCreatedDate)
        val itemColorBtn: Button = itemView.findViewById(R.id.buttonItemColor)
        val dateName: TextView = itemView.findViewById(R.id.textViewDateTxt)

    }

    fun getItemAt(position: Int): ItemSold {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem: ItemSold = getItemAt(position)
        val dateFormat: DateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ROOT)

        holder.itemName.text = currentItem.itemName
        holder.itemCategory.text = getCatAsStr(currentItem.itemCategory)
        holder.itemPrice.text = currentItem.itemPrice.toString()
        holder.itemQuantity.text = currentItem.itemQuantity.toString()
        holder.itemTotal.text = currentItem.itemTotal.toString()
        holder.itemCreatedDate.text = dateFormat.format(currentItem.dateSold)

        holder.dateName.text = "Продано"
        holder.itemColorBtn.setBackgroundColor(currentItem.itemColor)
    }

    fun getCatAsStr(code: Int): String {
        return when (code) {
            CAT_VESNA -> CAT_VESNA_TXT
            CAT_LETO -> CAT_LETO_TXT
            CAT_OSEN -> CAT_OSEN_TXT
            CAT_SHKOLNYI -> CAT_SHKOLNYI_TXT
            CAT_ZIMA -> CAT_ZIMA_TXT
            else -> CAT_DRUGUIE_TXT
        }
    }
}