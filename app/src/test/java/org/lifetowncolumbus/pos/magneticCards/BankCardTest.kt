package org.lifetowncolumbus.pos.magneticCards

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class BankCardTest {

    @Test
    fun emptyString_ShouldNot_BeValid() {
        val result = BankCard("").isValid
        assertThat(result, `is`(false))
    }

    @Test
    fun correctString_Should_BeValid() {
        val result = BankCard(";1111111100000061��?").isValid
        assertThat(result, `is`(true))
    }

    @Test
    fun correctString_Should_SetTheAccountNumber() {
        val result = BankCard(";1111111100000061��?").accountNumber
        assertThat(result, `is`("1111111100000061"))
    }


}