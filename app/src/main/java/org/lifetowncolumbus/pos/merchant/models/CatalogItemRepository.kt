package org.lifetowncolumbus.pos.merchant.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.annotation.WorkerThread

class CatalogItemRepository(private val catalogItemDao: CatalogItemDao) {
    val allItems: LiveData<List<CatalogItem>> = catalogItemDao.getAllItems()

    @WorkerThread
    fun addItem(catalogItem: CatalogItem) {
        catalogItemDao.insert(catalogItem)
    }
}
