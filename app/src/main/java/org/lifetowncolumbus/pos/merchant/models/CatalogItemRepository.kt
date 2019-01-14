package org.lifetowncolumbus.pos.merchant.models

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread

class CatalogItemRepository(private val catalogItemDao: CatalogItemDao) {
    val allItems: LiveData<List<CatalogItem>> = catalogItemDao.getAllItems()

    @WorkerThread
    suspend fun addItem(catalogItem: CatalogItem) {
        catalogItemDao.insert(catalogItem)
    }
}
