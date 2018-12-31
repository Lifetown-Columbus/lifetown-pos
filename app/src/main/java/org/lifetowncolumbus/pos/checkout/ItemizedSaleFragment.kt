package org.lifetowncolumbus.pos.checkout

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.lifetowncolumbus.pos.R
import java.math.BigDecimal

class ItemizedSaleFragement : Fragment() {

    lateinit var totalValue: TextView
    lateinit var checkout: Checkout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalValue = view.findViewById(R.id.total)
        renderTotal()

        checkout.items.observe(this, Observer {
            renderTotal()
        })
    }

    private fun renderTotal() {
        val text = when {
            checkout.total < BigDecimal.ZERO -> "Change Due: ${(checkout.total.abs()).toCurrencyString()}"
            else -> "Total: ${checkout.total.toCurrencyString()}"

        }
        totalValue.apply {
            this.text = text
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_itemized_sale, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkout = activity?.run {
            ViewModelProviders.of(this).get(Checkout::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}
