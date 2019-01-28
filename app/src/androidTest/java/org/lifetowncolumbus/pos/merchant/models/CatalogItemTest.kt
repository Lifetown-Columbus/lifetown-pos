package org.lifetowncolumbus.pos.merchant.models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.emptyCollectionOf
import org.hamcrest.Matchers.equalTo
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


}

