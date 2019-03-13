package org.lifetowncolumbus.pos.printing

import org.lifetowncolumbus.pos.toCurrencyString
import java.math.BigDecimal

fun serializeReceiptItem(
    name: String,
    value: BigDecimal,
    length: Int): String {

    val valueString = value.toCurrencyString()
    var mutableName = name
    var padding = ""
    var paddingLength = length - name.length - valueString.length

    if (paddingLength < 3) {
        mutableName = name.dropLast(3 - paddingLength)
        paddingLength = 3
    }

    repeat(paddingLength) { padding += '.'}

    return "$mutableName$padding$valueString\n"
}
