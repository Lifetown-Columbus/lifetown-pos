package org.lifetowncolumbus.pos.merchant.models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.lifetowncolumbus.pos.merchant.TestHarness
import org.lifetowncolumbus.pos.merchant.blockingObserve

class CatalogItemDbTest : TestHarness() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun deleteAllShouldDeleteEverything() {
        val item1 = CatalogItem(null, "Foo", 10.0)
        val item2 = CatalogItem(null, "Bar", 10.0)

        catalogItemDao.insert(item1)
        catalogItemDao.insert(item2)
        catalogItemDao.deleteAll()
        val result = catalogItemDao.getAllItems().blockingObserve()

        assertThat(result, emptyCollectionOf(CatalogItem::class.java))
    }

    @Test
    fun shouldSaveAndRetrieveCatalogItems() {
        val item = CatalogItem(9999, "Foo", 10.0)

        catalogItemDao.insert(item)
        val result = catalogItemDao.getAllItems().blockingObserve()

        assertThat(result?.get(0), equalTo(item))
    }

    @Test
    fun shouldUpdateAnItem() {
        val item = CatalogItem(9999, "Foo", 10.0)
        catalogItemDao.insert(item)

        item.apply {
            name = "Bar"
            value = 5.0
        }

        catalogItemDao.update(item)
        val result = catalogItemDao.getAllItems().blockingObserve()

        assertThat(result?.get(0), equalTo(item))
    }

    @Test
    fun shouldFindAndItemByPrimaryKey() {
        val item = CatalogItem(9999, "Foo", 10.0)
        val item2 = CatalogItem(9998, "Derp", 10.0)

        catalogItemDao.insert(item)
        catalogItemDao.insert(item2)
        val result = catalogItemDao.find(9998).blockingObserve()

        assertThat(result, equalTo(item2))
    }

    @Test
    fun shouldDeleteItem() {
        val item = CatalogItem(9999, "Foo", 10.0)

        catalogItemDao.insert(item)
        catalogItemDao.delete(item)
        val result = catalogItemDao.find(9999).blockingObserve()

        assertThat(result, `is`(nullValue()))
    }


}

