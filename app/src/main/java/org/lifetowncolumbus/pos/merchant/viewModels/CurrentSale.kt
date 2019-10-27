package org.lifetowncolumbus.pos.merchant.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.lifetowncolumbus.pos.R
import java.math.BigDecimal

class CurrentSale : ViewModel() {

    val canCheckout: MutableLiveData<Boolean> = MutableLiveData(false)
    var currentDestination: Int = 0
        set(destinationId) {
            field = destinationId
            updateCanCheckout()
        }

    private fun updateCanCheckout() {
        canCheckout.postValue(currentDestination == R.id.checkoutFragment && total > BigDecimal.ZERO)
    }

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
        updateCanCheckout()
    }

    fun removeItem(position: Int) {
        _items.value?.removeAt(position)
        _items.postValue(_items.value)
        updateCanCheckout()
    }

    fun payCash(payment: CashPayment) {
        addItem(payment)
    }

    fun payCredit(payment: CreditPayment) {
        addItem(payment)
    }

    fun newSale() {
        _items.postValue(ArrayList())
    }
}
