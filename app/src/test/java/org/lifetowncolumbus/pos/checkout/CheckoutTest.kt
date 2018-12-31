package org.lifetowncolumbus.pos.checkout

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.math.BigDecimal

class CheckoutTest {

    private lateinit var subject: Checkout
    @Mock lateinit var observer: Observer<ArrayList<Item>>

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        subject = Checkout()
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
}