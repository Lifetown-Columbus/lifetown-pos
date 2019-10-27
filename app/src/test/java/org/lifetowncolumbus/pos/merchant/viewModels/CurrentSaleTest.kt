package org.lifetowncolumbus.pos.merchant.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.lifetowncolumbus.pos.R
import java.math.BigDecimal

class CurrentSaleTest {

    private lateinit var subject: CurrentSale
    private val itemsObserver: Observer<ArrayList<Item>> = mockk(relaxUnitFun = true)
    private val canCheckoutObserver: Observer<Boolean> = mockk(relaxUnitFun = true)

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        subject = CurrentSale()
        subject.items.observeForever(itemsObserver)
        subject.canCheckout.observeForever(canCheckoutObserver)
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

        verify(exactly = 3) { itemsObserver.onChanged(withArg { it.containsAll(listOf(item, item2))}) }
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

    @Test
    fun setCurrentDestination_setsTheCurrentDestination() {
        subject.currentDestination = 2
        assertThat(subject.currentDestination, `is`(2))
    }

    @Test
    fun canCheckout_isTrue_whenBalanceGreaterThanZero_and_onCheckoutFragment() {
        subject.currentDestination = R.id.checkoutFragment
        subject.addItem(PurchasedItem.worth(1.0))
        subject.removeItem(0)

        verifySequence {
            canCheckoutObserver.onChanged(false) //constuctor
            canCheckoutObserver.onChanged(false)
            canCheckoutObserver.onChanged(true)
            canCheckoutObserver.onChanged(false)
        }

        assertThat(subject.canCheckout.value, `is`(false))
    }
}