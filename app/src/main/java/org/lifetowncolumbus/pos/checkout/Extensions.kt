package org.lifetowncolumbus.pos.checkout

import java.math.BigDecimal
import java.text.NumberFormat

fun BigDecimal.toCurrencyString(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}