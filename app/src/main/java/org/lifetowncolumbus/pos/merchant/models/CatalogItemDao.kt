package org.lifetowncolumbus.pos.merchant.models

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface CatalogItemDao {
    @Query("Select * from catalog")
    fun getAllItems() : LiveData<List<CatalogItem>>

    @Insert
    fun insert(catalogItem: CatalogItem)
}
