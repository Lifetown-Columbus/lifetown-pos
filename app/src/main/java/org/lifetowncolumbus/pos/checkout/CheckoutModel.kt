package org.lifetowncolumbus.pos.checkout

import java.math.BigDecimal

class CheckoutModel {
    val items: ArrayList<Item> = ArrayList()

    fun getTotal(): BigDecimal {
        return items.map { it.value }.fold(BigDecimal.ZERO, BigDecimal::add)
    }

    fun addItem(item: Item) {
        items.add(item)
    }
}
