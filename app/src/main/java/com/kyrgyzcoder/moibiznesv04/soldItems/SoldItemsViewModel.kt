package com.kyrgyzcoder.moibiznesv04.soldItems

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kyrgyzcoder.moibiznesv04.dataItemsSold.ItemSold
import com.kyrgyzcoder.moibiznesv04.dataItemsSold.ItemSoldRepository

class SoldItemsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ItemSoldRepository(application)
    private val soldAll: LiveData<List<ItemSold>> = repo.getAllSoldItemsNew()
    private val soldVesna: LiveData<List<ItemSold>> = repo.getVesnaSold()
    private val soldLeto: LiveData<List<ItemSold>> = repo.getLetoSold()
    private val soldOsen: LiveData<List<ItemSold>> = repo.getOsenSold()
    private val soldShkolnyi: LiveData<List<ItemSold>> = repo.getShkolnyiSold()
    private val soldZima: LiveData<List<ItemSold>> = repo.getZimaSold()
    private val soldDrugie: LiveData<List<ItemSold>> = repo.getDrugieSold()

    fun insertSoldItem(item: ItemSold) {
        repo.insertSoldItem(item)
    }

    fun getAllSoldItems(): LiveData<List<ItemSold>> {
        return soldAll
    }

    fun deleteSoldItem(item: ItemSold) {
        repo.deleteSoldItem(item)
    }

    fun getVesnaSold(): LiveData<List<ItemSold>> {
        return soldVesna
    }

    fun getLetoSold(): LiveData<List<ItemSold>> {
        return soldLeto
    }

    fun getOsenSold(): LiveData<List<ItemSold>> {
        return soldOsen
    }

    fun getShkolnyiSold(): LiveData<List<ItemSold>> {
        return soldShkolnyi
    }

    fun getZimaSold(): LiveData<List<ItemSold>> {
        return soldZima
    }

    fun getDrugieSold(): LiveData<List<ItemSold>> {
        return soldDrugie
    }
}
