package org.lifetowncolumbus.pos.checkout

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
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
        subject.addItem(Item(BigDecimal.valueOf(1.0)))
        subject.addItem(Item(BigDecimal.valueOf(2.0)))

        assertThat(subject.total, `is`(BigDecimal.valueOf(3.0)))
    }

    @Test
    fun addingItem_isObservable() {
        val item = Item(BigDecimal.valueOf(1.0))
        subject.addItem(item)

        verify(observer).onChanged(argThat { it!!.containsAll(listOf(item))})
    }

    @Test
    fun calculateChange_returnsCorrectValue() {
        assertThat(subject.calculateChange(BigDecimal.valueOf(5.0)), `is`(BigDecimal.valueOf(5.0)))

        subject.addItem(Item(BigDecimal.valueOf(1.0)))

        assertThat(subject.calculateChange(BigDecimal.valueOf(5.0)), `is`(BigDecimal.valueOf(4.0)))
    }

}