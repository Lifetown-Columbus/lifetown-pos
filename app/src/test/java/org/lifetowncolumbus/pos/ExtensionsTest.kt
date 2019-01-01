package org.lifetowncolumbus.pos

import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.lifetowncolumbus.pos.toCurrencyString
import java.math.BigDecimal

class ExtensionsTest {
    @Test
    fun bigDecimalsValues_canBeConvertedToACurrencyString() {
        val bigDecimal: BigDecimal = BigDecimal.valueOf(22.0)

        assertThat(bigDecimal.toCurrencyString(), `is`("$22.00"))
    }
}

