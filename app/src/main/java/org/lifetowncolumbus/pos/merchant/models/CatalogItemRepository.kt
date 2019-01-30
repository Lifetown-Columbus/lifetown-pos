package org.lifetowncolumbus.pos.merchant.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.annotation.WorkerThread

class CatalogItemRepository(private val catalogItemDao: CatalogItemDao) {
    val allItems: LiveData<List<CatalogItem>> = catalogItemDao.getAllItems()

    @WorkerThread
    fun saveItem(catalogItem: CatalogItem) {
        if(catalogItem.id == null) {
            catalogItemDao.insert(catalogItem)
        } else {
            catalogItemDao.update(catalogItem)
        }
    }

    fun find(id: Long) : LiveData<CatalogItem> {
        return catalogItemDao.find(id)
    }

}
