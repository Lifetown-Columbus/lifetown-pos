package org.lifetowncolumbus.pos.merchant.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.lifetowncolumbus.pos.merchant.models.CatalogItem
import org.lifetowncolumbus.pos.merchant.models.CatalogItemRepository
import org.lifetowncolumbus.pos.merchant.models.LocalDatabase
import kotlin.coroutines.CoroutineContext

class Catalog(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)
    private val repository: CatalogItemRepository

    val allItems: LiveData<List<CatalogItem>>

    init {
        val catalogItemDao = LocalDatabase.getInstance(application).catalogItemDao()
        repository = CatalogItemRepository(catalogItemDao)
        allItems = repository.allItems
    }

    fun saveItem(catalogItem: CatalogItem) = scope.launch(Dispatchers.IO) {
        repository.saveItem(catalogItem)
    }

    fun find(id: Long) : LiveData<CatalogItem> {
        return repository.find(id)
    }

    fun delete(catalogItem: CatalogItem) = scope.launch(Dispatchers.IO) {
        repository.delete(catalogItem)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}