package org.lifetowncolumbus.pos.merchant.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.math.BigDecimal

class CurrentSaleTest {

    private lateinit var subject: CurrentSale
    @Mock lateinit var observer: Observer<ArrayList<Item>>

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        subject = CurrentSale()
        subject.items.observeForever(observer)
    }

    @Test
    fun itIsAViewModel() {
        assertThat(subject, `is`(instanceOf(ViewModel::class.java)))
    }

    @Test
    fun createModel_setsTheTotalToZero() {
        assertThat(subject.total, `is`(BigDecimal.valueOf(0)))
    }

    @Test
    fun addingItem_updatesTotal() {
        subject.addItem(PurchasedItem.worth(1.0))
        subject.addItem(PurchasedItem.worth(2.0))

        assertThat(subject.total, `is`(BigDecimal.valueOf(3.0)))
    }

    @Test
    fun addingItem_isObservable() {
        val item = PurchasedItem.worth(1.0)
        val item2 = PurchasedItem.worth(3.0)
        subject.addItem(item)
        subject.addItem(item2)

        verify(observer, times(3)).onChanged(argThat { it!!.containsAll(listOf(item, item2))})
    }

    @Test
    fun payCash_updatesTheTotal() {
        subject.addItem(PurchasedItem.worth(3.0))

        subject.payCash(CashPayment.worth(4.0))

        assertThat(subject.total, `is`(BigDecimal.valueOf(-1.0)))
    }

    @Test
    fun newSale_clearsItems() {
        subject.addItem(PurchasedItem.worth(3.0))
        subject.payCash(CashPayment.worth(4.0))

        subject.newSale()

        assertThat(subject.items.value, `is`(emptyCollectionOf(Item::class.java)))

    }
}