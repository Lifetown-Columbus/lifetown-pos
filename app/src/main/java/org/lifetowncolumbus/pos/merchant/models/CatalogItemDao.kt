package org.lifetowncolumbus.pos.merchant.models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CatalogItemDao {
    @Query("Select * from catalog")
    fun getAllItems() : LiveData<List<CatalogItem>>

    @Insert
    fun insert(catalogItem: CatalogItem)

    @Query("Delete from catalog")
    fun deleteAll()

    @Update
    fun update(catalogItem: CatalogItem)

    @Query("SELECT * FROM catalog WHERE id == :id")
    fun find(id: Long) : LiveData<CatalogItem>
}
