package com.kyrgyzcoder.moibiznesv04.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kyrgyzcoder.moibiznesv04.*
import com.kyrgyzcoder.moibiznesv04.dataItemsAv.Item
import com.kyrgyzcoder.moibiznesv04.utils.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ItemsRecyclerViewAdapter(private var listener: OnItemClickListenerN) :
    ListAdapter<Item, ItemsRecyclerViewAdapter.MyViewHolder>(
        DIFF_CALLBACK
    ) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return (oldItem.itemName == newItem.itemName &&
                        oldItem.itemPrice == newItem.itemPrice &&
                        oldItem.itemQuantity == newItem.itemQuantity &&
                        oldItem.itemCategory == newItem.itemCategory &&
                        oldItem.itemColor == newItem.itemColor)
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val itemName: TextView = itemView.findViewById(R.id.textViewItemName)
        val itemQuantity: TextView = itemView.findViewById(R.id.textViewItemQuantity)
        val itemPrice: TextView = itemView.findViewById(R.id.textViewItemPrice)
        val itemCategory: TextView = itemView.findViewById(R.id.textViewItemCategory)
        val itemTotal: TextView = itemView.findViewById(R.id.textViewItemTotalPrice)
        val itemCreatedDate: TextView = itemView.findViewById(R.id.textViewItemCreatedDate)
        val itemColorBtn: Button = itemView.findViewById(R.id.buttonItemColor)
        val dateName: TextView = itemView.findViewById(R.id.textViewDateTxt)

        val mLayout: CardView = itemView.findViewById(R.id.list_items)

        init {
            mLayout.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener?.onItemClick(adapterPosition)
        }
    }

    interface OnItemClickListenerN {
        fun onItemClick(position: Int)
    }

    fun getItemAt(position: Int): Item {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem: Item = getItemAt(position)
        val dateFormat: DateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ROOT)
        holder.itemName.text = currentItem.itemName
        holder.itemCategory.text = getCatAsStr(currentItem.itemCategory)
        holder.itemPrice.text = currentItem.itemPrice.toString()
        holder.itemQuantity.text = currentItem.itemQuantity.toString()
        val total: Int = currentItem.itemPrice * currentItem.itemQuantity
        holder.itemTotal.text = total.toString()
        holder.itemCreatedDate.text = dateFormat.format(currentItem.dateCreated)
        holder.itemColorBtn.setBackgroundColor(currentItem.itemColor)
        holder.dateName.text = "Добавлено"
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