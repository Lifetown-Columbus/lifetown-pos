package org.lifetowncolumbus.pos.checkout

import android.arch.lifecycle.ViewModel
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class CheckoutTest {

    private lateinit var subject: Checkout

    @Before
    fun setup() {
       subject = Checkout()
    }


    @Test
    fun createModel_setsTheTotalToZero() {
        assertThat(subject.getTotal(), `is`(BigDecimal.valueOf(0)))
    }

    @Test
    fun addingItem_updatesTotal() {
        subject.addItem(Item(BigDecimal.valueOf(1.0)))
        subject.addItem(Item(BigDecimal.valueOf(2.0)))

        assertThat(subject.getTotal(), `is`(BigDecimal.valueOf(3.0)))
    }

    @Test
    fun itIsAViewModel() {
        assertThat(subject, `is`(instanceOf(ViewModel::class.java)))
    }

}