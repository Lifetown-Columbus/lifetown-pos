package org.lifetowncolumbus.pos.checkout

import android.arch.lifecycle.ViewModel
import java.math.BigDecimal

class Checkout : ViewModel() {
    val items: ArrayList<Item> = ArrayList()
    val total: BigDecimal
        get() = items.map { it.value }.fold(BigDecimal.ZERO, BigDecimal::add)

    fun addItem(item: Item) {
        items.add(item)
    }

    fun calculateChange(amountTendered: BigDecimal): BigDecimal {
        return amountTendered.subtract(total)
    }
}
