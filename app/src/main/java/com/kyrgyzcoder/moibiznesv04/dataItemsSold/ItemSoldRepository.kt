package com.kyrgyzcoder.moibiznesv04.dataItemsSold

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.kyrgyzcoder.moibiznesv04.datamodel.ItemsDatabase

class ItemSoldRepository(app: Application) {

    private var itemSoldDAO: ItemSoldDAO
    private var allItemsSoldNew: LiveData<List<ItemSold>>
    private var allItemSoldOld: LiveData<List<ItemSold>>
    private var vesnaItemsSold: LiveData<List<ItemSold>>
    private var letoItemsSold: LiveData<List<ItemSold>>
    private var osenItemsSold: LiveData<List<ItemSold>>
    private var shkolnyiItemsSold: LiveData<List<ItemSold>>
    private var zimaItemsSold: LiveData<List<ItemSold>>
    private var drugieItemsSold: LiveData<List<ItemSold>>

    init {
        val db: ItemsDatabase = ItemsDatabase.getInstance(app.applicationContext)!!
        itemSoldDAO = db.ItemSoldDao()
        allItemsSoldNew = itemSoldDAO.getAllSoldItemsNew()
        allItemSoldOld = itemSoldDAO.getAllSoldItemsOld()
        vesnaItemsSold = itemSoldDAO.getSoldVesna()
        letoItemsSold = itemSoldDAO.getSoldLeto()
        osenItemsSold = itemSoldDAO.getSoldOsen()
        shkolnyiItemsSold = itemSoldDAO.getSoldShkola()
        zimaItemsSold = itemSoldDAO.getSoldZima()
        drugieItemsSold = itemSoldDAO.getSoldDrugie()
    }

    fun getAllSoldItemsNew(): LiveData<List<ItemSold>> {
        return allItemsSoldNew
    }

    fun getAllSoldItemsOld(): LiveData<List<ItemSold>> {
        return allItemSoldOld
    }

    fun getVesnaSold(): LiveData<List<ItemSold>> {
        return vesnaItemsSold
    }

    fun getLetoSold(): LiveData<List<ItemSold>> {
        return letoItemsSold
    }

    fun getOsenSold(): LiveData<List<ItemSold>> {
        return osenItemsSold
    }

    fun getShkolnyiSold(): LiveData<List<ItemSold>> {
        return shkolnyiItemsSold
    }

    fun getZimaSold(): LiveData<List<ItemSold>> {
        return zimaItemsSold
    }

    fun getDrugieSold(): LiveData<List<ItemSold>> {
        return drugieItemsSold
    }

    fun insertSoldItem(itemSold: ItemSold) {
        InsertItemSoldAsyncTask(itemSoldDAO).execute(itemSold)
    }

    fun deleteSoldItem(itemSold: ItemSold) {
        DeleteItemSoldAsyncTask(itemSoldDAO).execute(itemSold)
    }

    fun deleteAllSoldItems() {
        DeleteAllItemsSoldAsyncTask(itemSoldDAO).execute()
    }

    companion object {
        class InsertItemSoldAsyncTask(private val itemSoldDAO: ItemSoldDAO) :
            AsyncTask<ItemSold, Unit, Unit>() {
            override fun doInBackground(vararg params: ItemSold?) {
                itemSoldDAO.insertSoldItem(params[0]!!)
            }
        }

        class DeleteItemSoldAsyncTask(private val itemSoldDAO: ItemSoldDAO) :
            AsyncTask<ItemSold, Unit, Unit>() {
            override fun doInBackground(vararg params: ItemSold?) {
                itemSoldDAO.deleteSoldItem(params[0]!!)
            }
        }

        class DeleteAllItemsSoldAsyncTask(private val itemSoldDAO: ItemSoldDAO) :
            AsyncTask<Unit, Unit, Unit>() {

            override fun doInBackground(vararg params: Unit?) {
                itemSoldDAO.deleteAllSoldItems()
            }
        }
    }
}