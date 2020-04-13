package com.kyrgyzcoder.moibiznesv04.dataItemsAv

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "items_available_table")
data class Item(
    var itemName: String,
    var itemSize: String,
    var itemColor: Int,
    var itemCategory: Int,
    var itemPrice: Int,
    var itemQuantity: Int,
    var dateCreated: Date
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}