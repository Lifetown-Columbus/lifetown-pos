package org.lifetowncolumbus.pos.printing

import org.hamcrest.CoreMatchers.endsWith
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal

class TextFormatterTest {

    @Test
    fun itConvertsTheValueToACurrencyString() {
        val result = serializeReceiptItem(name = "Item Name", value = BigDecimal.valueOf(10.00), length = 20)
        assertThat(result, endsWith("$10.00\n"))
    }

    @Test
    fun itPrintsTheItemName() {
        val result = serializeReceiptItem(name = "Item Name", value = BigDecimal.valueOf(10.00), length = 20)
        assertThat(result, startsWith("Item Name"))
    }

    @Test
    fun itPadsTheCenterSpaceWithDots() {
        val result = serializeReceiptItem(name = "Item Name", value = BigDecimal.valueOf(10.00), length = 20)
        assertThat(result, `is`("Item Name.....$10.00\n"))
    }

    @Test
    fun itTruncatesTheItemName() {
        val result = serializeReceiptItem(name = "Item Name", value = BigDecimal.valueOf(10.00), length = 15)
        assertThat(result, `is`("Item N...$10.00\n"))
    }
}