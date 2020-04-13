package com.kyrgyzcoder.moibiznesv04.dataItemsSold

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemSoldDAO {
    @Insert
    fun insertSoldItem(itemSold: ItemSold)

    @Delete
    fun deleteSoldItem(itemSold: ItemSold)

    @Query("SELECT * FROM items_sold_table ORDER BY id DESC")
    fun getAllSoldItemsNew(): LiveData<List<ItemSold>>

    @Query("SELECT * FROM items_sold_table ORDER BY id ASC")
    fun getAllSoldItemsOld(): LiveData<List<ItemSold>>

    @Query("DELETE FROM items_sold_table")
    fun deleteAllSoldItems()

    @Query("SELECT * FROM items_sold_table WHERE itemCategory==2222 ORDER BY id DESC")
    fun getSoldLeto(): LiveData<List<ItemSold>>

    @Query("SELECT * FROM items_sold_table WHERE itemCategory==1111 ORDER BY id DESC")
    fun getSoldVesna(): LiveData<List<ItemSold>>

    @Query("SELECT * FROM items_sold_table WHERE itemCategory==3333 ORDER BY id DESC")
    fun getSoldOsen(): LiveData<List<ItemSold>>

    @Query("SELECT * FROM items_sold_table WHERE itemCategory==4444 ORDER BY id DESC")
    fun getSoldShkola(): LiveData<List<ItemSold>>

    @Query("SELECT * FROM items_sold_table WHERE itemCategory==5555 ORDER BY id DESC")
    fun getSoldZima(): LiveData<List<ItemSold>>

    @Query("SELECT * FROM items_sold_table WHERE itemCategory==6666 ORDER BY id DESC")
    fun getSoldDrugie(): LiveData<List<ItemSold>>
}