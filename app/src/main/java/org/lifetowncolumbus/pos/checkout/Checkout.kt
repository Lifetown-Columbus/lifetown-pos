package org.lifetowncolumbus.pos.checkout

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import java.math.BigDecimal

class Checkout : ViewModel() {
    val items: MutableLiveData<ArrayList<Item>> by lazy {
        MutableLiveData<ArrayList<Item>>().apply {
            value = ArrayList()
        }
    }
    val total: BigDecimal
        get() = items.value!!.map { it.value }.fold(BigDecimal.ZERO, BigDecimal::add)

    fun addItem(item: Item) {
        items.value?.add(item)
        //force onChange to fire
        items.value = items.value
    }

    fun calculateChange(amountTendered: BigDecimal): BigDecimal {
        return amountTendered.subtract(total)
    }
}
