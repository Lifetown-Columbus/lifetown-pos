package org.lifetowncolumbus.pos.checkout

import org.hamcrest.Matchers
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class CheckoutModelTest {

    private lateinit var subject: CheckoutModel

    @Before
    fun setup() {
       subject = CheckoutModel()
    }


    @Test
    fun createModel_setsTheTotalToZero() {
        assertThat(subject.getTotal(), Matchers.`is`(BigDecimal.valueOf(0)))
    }

    @Test
    fun addingItem_updatesTotal() {
        subject.addItem(Item(BigDecimal.valueOf(1.0)))
        subject.addItem(Item(BigDecimal.valueOf(2.0)))

        assertThat(subject.getTotal(), Matchers.`is`(BigDecimal.valueOf(3.0)))
    }



}