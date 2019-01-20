package org.lifetowncolumbus.pos.merchant

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.runner.RunWith
import org.lifetowncolumbus.pos.merchant.models.CatalogItemDao
import org.lifetowncolumbus.pos.merchant.models.LocalDatabase

@RunWith(AndroidJUnit4::class)
abstract class TestHarness {
    protected lateinit var catalogItemDao: CatalogItemDao
    private lateinit var db: LocalDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = LocalDatabase.getInstance(context)
        catalogItemDao = db.catalogItemDao()
        catalogItemDao.deleteAll()
    }


}