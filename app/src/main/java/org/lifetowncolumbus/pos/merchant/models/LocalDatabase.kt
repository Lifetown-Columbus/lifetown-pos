package org.lifetowncolumbus.pos.merchant.models

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [CatalogItem::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun catalogItemDao(): CatalogItemDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(LocalDatabase::class) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    LocalDatabase::class.java, "local_database")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
