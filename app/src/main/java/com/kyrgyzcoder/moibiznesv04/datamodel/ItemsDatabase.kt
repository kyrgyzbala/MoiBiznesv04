package com.kyrgyzcoder.moibiznesv04.datamodel

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kyrgyzcoder.moibiznesv04.utils.DateConverter
import com.kyrgyzcoder.moibiznesv04.dataItemsAv.Item
import com.kyrgyzcoder.moibiznesv04.dataItemsAv.ItemDAO
import com.kyrgyzcoder.moibiznesv04.dataItemsSold.ItemSold
import com.kyrgyzcoder.moibiznesv04.dataItemsSold.ItemSoldDAO

@Database(entities = [Item::class, ItemSold::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class ItemsDatabase : RoomDatabase() {

    abstract fun ItemAvDao(): ItemDAO
    abstract fun ItemSoldDao(): ItemSoldDAO

    companion object {
        private var instance: ItemsDatabase? = null

        fun getInstance(context: Context): ItemsDatabase? {
            if (instance == null) {
                synchronized(ItemsDatabase::class) {
                    instance = Room.databaseBuilder(
                        context,
                        ItemsDatabase::class.java,
                        "item_database"
                    ).fallbackToDestructiveMigration()
                        .addCallback(RoomCallback)
                        .build()
                }
            }
            return instance
        }

        private val RoomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CreateDbAsyncTask(instance).execute()
            }
        }

        class CreateDbAsyncTask(db: ItemsDatabase?) : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                /*In case you want to add some data to database at the beginning
                        val TovarDao = db?.TovarDao()
                        tovarDao?.insert(Tovar("" parameters))
                     */
            }
        }

    }
}