package org.lifetowncolumbus.pos.checkout

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.math.BigDecimal

class ItemTest {
    @Test
    fun purchasedItems_haveAValue() {
        val subject: Item = PurchasedItem.worth(2.0)

        assertThat(subject.value, `is`(BigDecimal.valueOf(2.0)))
    }

    @Test
    fun cashPayments_haveANegativeValue() {
        val subject: Item = CashPayment.worth(2.0)

        assertThat(subject.value, `is`(BigDecimal.valueOf(-2.0)))
    }

    @Test
    fun purchasedItems_haveAName() {
        val subject: Item = PurchasedItem.worth(2.0)

        assertThat(subject.name, `is`("Purchased Item"))
    }

    @Test
    fun casePayment_hasAName() {
        val subject: Item = CashPayment.worth(2.0)

        assertThat(subject.name, `is`("Cash Payment"))
    }
}