package org.lifetowncolumbus.pos.checkout

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import java.math.BigDecimal

class Checkout : ViewModel() {
    private val mItems: MutableLiveData<ArrayList<Item>> by lazy {
        MutableLiveData<ArrayList<Item>>().apply {
            value = ArrayList()
        }
    }
    val items: LiveData<ArrayList<Item>> = mItems

    val total: BigDecimal
        get() = items.value!!.map { it.value }.fold(BigDecimal.ZERO, BigDecimal::add)

    fun addItem(item: Item) {
        mItems.value?.add(item)
        //force onChange to fire
        mItems.postValue(mItems.value)
    }

    fun payCash(payment: CashPayment) {
        addItem(payment)
    }
}
