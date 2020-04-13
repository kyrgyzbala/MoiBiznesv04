package com.kyrgyzcoder.moibiznesv04.dataItemsSold

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "items_sold_table")
data class ItemSold(
    var itemName: String,
    var itemSize: String,
    var itemColor: Int,
    var itemCategory: Int,
    var itemPrice: Int,
    var itemTotal: Int,
    var itemQuantity: Int,
    var dateSold: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}