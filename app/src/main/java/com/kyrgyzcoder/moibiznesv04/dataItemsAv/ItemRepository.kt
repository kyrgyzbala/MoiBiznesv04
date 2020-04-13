package com.kyrgyzcoder.moibiznesv04.dataItemsAv

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.kyrgyzcoder.moibiznesv04.datamodel.ItemsDatabase

class ItemRepository(app: Application) {

    private var itemDAO: ItemDAO
    private var allItemsNew: LiveData<List<Item>>
    private var allItemsOld: LiveData<List<Item>>
    private var itemsVesna: LiveData<List<Item>>
    private var itemsLeto: LiveData<List<Item>>
    private var itemsOsen: LiveData<List<Item>>
    private var itemsShkolnyi: LiveData<List<Item>>
    private var itemsZima: LiveData<List<Item>>
    private var itemsDrugie: LiveData<List<Item>>


    init {
        val db: ItemsDatabase = ItemsDatabase.getInstance(app.applicationContext)!!
        itemDAO = db.ItemAvDao()
        allItemsNew = itemDAO.getAllItemsNewOnTop()
        allItemsOld = itemDAO.getAllItemsOldOnTop()
        itemsVesna = itemDAO.getVesnaItems()
        itemsLeto = itemDAO.getLetoItems()
        itemsOsen = itemDAO.getOsenItems()
        itemsShkolnyi = itemDAO.getShkolnyiItems()
        itemsZima = itemDAO.getZimaItems()
        itemsDrugie = itemDAO.getDrugieItems()
    }

    fun getAllItemsNewOnTop(): LiveData<List<Item>> {
        return allItemsNew
    }

    fun getAllItemsOldOnTop(): LiveData<List<Item>> {
        return allItemsOld
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
        InsertItemAsyncTask(itemDAO).execute(item)
    }

    fun updateItem(item: Item) {
        UpdateItemAsyncTask(itemDAO).execute(item)
    }

    fun deleteItem(item: Item) {
        DeleteItemAsyncTask(itemDAO).execute(item)
    }

    fun deleteAllItems() {
        DeleteAllItemsAsyncTask(itemDAO).execute()
    }


    companion object {
        class InsertItemAsyncTask(private val itemDAO: ItemDAO) : AsyncTask<Item, Unit, Unit>() {
            override fun doInBackground(vararg params: Item?) {
                itemDAO.insertItem(params[0]!!)
            }
        }

        class UpdateItemAsyncTask(private val itemDAO: ItemDAO) : AsyncTask<Item, Unit, Unit>() {
            override fun doInBackground(vararg params: Item?) {
                itemDAO.updateItem(params[0]!!)
            }
        }

        class DeleteItemAsyncTask(private val itemDAO: ItemDAO) : AsyncTask<Item, Unit, Unit>() {
            override fun doInBackground(vararg params: Item?) {
                itemDAO.deleteItem(params[0]!!)
            }
        }

        class DeleteAllItemsAsyncTask(private val itemDAO: ItemDAO) :
            AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                itemDAO.deleteAllItems()
            }
        }
    }

}