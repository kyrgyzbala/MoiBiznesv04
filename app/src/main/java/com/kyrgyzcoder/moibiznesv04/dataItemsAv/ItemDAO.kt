package com.kyrgyzcoder.moibiznesv04.dataItemsAv

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDAO {

    @Insert
    fun insertItem(item: Item)

    @Update
    fun updateItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    @Query("DELETE FROM items_available_table")
    fun deleteAllItems()

    @Query("SELECT * FROM items_available_table ORDER BY id DESC")
    fun getAllItemsNewOnTop(): LiveData<List<Item>>

    @Query("SELECT * FROM items_available_table ORDER BY id ASC")
    fun getAllItemsOldOnTop(): LiveData<List<Item>>

    @Query("SELECT * FROM items_available_table WHERE itemCategory==1111 ORDER BY id DESC")
    fun getVesnaItems(): LiveData<List<Item>>

    @Query("SELECT * FROM items_available_table WHERE itemCategory==2222 ORDER BY id DESC")
    fun getLetoItems(): LiveData<List<Item>>

    @Query("SELECT * FROM items_available_table WHERE itemCategory==3333 ORDER BY id DESC")
    fun getOsenItems(): LiveData<List<Item>>

    @Query("SELECT * FROM items_available_table WHERE itemCategory==4444 ORDER BY id DESC")
    fun getShkolnyiItems(): LiveData<List<Item>>

    @Query("SELECT * FROM items_available_table WHERE itemCategory==5555 ORDER BY id DESC")
    fun getZimaItems(): LiveData<List<Item>>

    @Query("SELECT * FROM items_available_table WHERE itemCategory==6666 ORDER BY id DESC")
    fun getDrugieItems(): LiveData<List<Item>>
}