package com.kyrgyzcoder.moibiznesv04.allItems

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kyrgyzcoder.moibiznesv04.dataItemsAv.Item
import com.kyrgyzcoder.moibiznesv04.dataItemsAv.ItemRepository

class AllItemsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ItemRepository(application)
    private val allItems: LiveData<List<Item>> = repo.getAllItemsNewOnTop()
    private val allItemsSorted: LiveData<List<Item>> = repo.getAllItemsOldOnTop()
    private var itemsVesna: LiveData<List<Item>> = repo.getVesnaItems()
    private var itemsLeto: LiveData<List<Item>> = repo.getLetoItems()
    private var itemsOsen: LiveData<List<Item>> = repo.getOsenItems()
    private var itemsShkolnyi: LiveData<List<Item>> = repo.getShkolnyiItems()
    private var itemsZima: LiveData<List<Item>> = repo.getZimaItems()
    private var itemsDrugie: LiveData<List<Item>> = repo.getDrugieItems()

    fun getAllItems(): LiveData<List<Item>> {
        return allItems
    }

    fun getAllSortedAs(): LiveData<List<Item>> {
        return allItemsSorted
    }

    fun getVesnaItems(): LiveData<List<Item>> {
        return itemsVesna
    }

    fun getLetoItems(): LiveData<List<Item>> {
        return itemsLeto
    }

    fun getOsenItems(): LiveData<List<Item>> {
        return itemsOsen
    }

    fun getShkolnyiItems(): LiveData<List<Item>> {
        return itemsShkolnyi
    }

    fun getZimaItems(): LiveData<List<Item>> {
        return itemsZima
    }

    fun getDrugieItems(): LiveData<List<Item>> {
        return itemsDrugie
    }

    fun insertItem(item: Item) {
        repo.insertItem(item)
    }

    fun updateItem(item: Item) {
        repo.updateItem(item)
    }

    fun deleteItem(item: Item) {
        repo.deleteItem(item)
    }

    fun deleteAllItems() {
        repo.deleteAllItems()
    }
}
