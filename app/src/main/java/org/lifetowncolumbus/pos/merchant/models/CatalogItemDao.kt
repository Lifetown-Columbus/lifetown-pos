package org.lifetowncolumbus.pos.merchant.models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CatalogItemDao {
    @Query("Select * from catalog")
    fun getAllItems() : LiveData<List<CatalogItem>>

    @Insert
    fun insert(catalogItem: CatalogItem)

    @Query("Delete from catalog")
    fun deleteAll()
}
