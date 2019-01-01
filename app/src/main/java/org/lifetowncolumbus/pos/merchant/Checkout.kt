package org.lifetowncolumbus.pos.merchant

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import java.math.BigDecimal

class Checkout : ViewModel() {
    private val _items: MutableLiveData<ArrayList<Item>> by lazy {
        MutableLiveData<ArrayList<Item>>().apply {
            value = ArrayList()
        }
    }
    val items: LiveData<ArrayList<Item>> = _items

    val total: BigDecimal
        get() = items.value!!.map { it.value }.fold(BigDecimal.ZERO, BigDecimal::add)

    fun addItem(item: Item) {
        _items.value?.add(item)
        _items.postValue(_items.value)
    }

    fun payCash(payment: CashPayment) {
        addItem(payment)
    }

    fun newSale() {
        _items.postValue(ArrayList())
    }
}