package org.lifetowncolumbus.pos.merchant.models

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.threeten.bp.LocalDate

class AccountTest {

    private val yesterday = LocalDate.now()
        .minusDays(1)

    @Test
    fun shouldExpireAtMidnight() {
        val account = Account("123", 0.0, yesterday.toEpochDay())
        assertThat(account.isExpired, `is`(true))
    }

    @Test
    fun shouldNotExpireIfCreatedToday() {
        val account = Account("123", 0.0, LocalDate.now().toEpochDay())
        assertThat(account.isExpired, `is`(false))
    }

    @Test
    fun shouldResetAccount() {
        val account = Account("123", 123.0, yesterday.toEpochDay())

        account.reset()

        assertThat(account.balance, `is`(0.0))
        assertThat(account.dayCreated, `is`(LocalDate.now().toEpochDay()))
    }

}