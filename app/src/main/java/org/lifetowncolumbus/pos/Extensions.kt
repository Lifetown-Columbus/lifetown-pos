package org.lifetowncolumbus.pos

import java.math.BigDecimal
import java.text.NumberFormat

fun BigDecimal.toCurrencyString(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}