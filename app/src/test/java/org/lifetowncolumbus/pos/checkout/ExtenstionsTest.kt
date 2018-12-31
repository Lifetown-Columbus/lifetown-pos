package org.lifetowncolumbus.pos.checkout

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.math.BigDecimal

class ExtenstionsTest {
    @Test
    fun bigDecimalsValues_canBeConvertedToACurrencyString() {
        val bigDecimal: BigDecimal = BigDecimal.valueOf(22.0)

        assertThat(bigDecimal.toCurrencyString(), `is`("$22.00"))
    }
}

