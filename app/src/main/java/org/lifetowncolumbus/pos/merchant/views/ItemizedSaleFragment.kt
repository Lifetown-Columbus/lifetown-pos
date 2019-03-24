package org.lifetowncolumbus.pos.merchant.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_itemized_sale.view.*
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.toCurrencyString
import java.math.BigDecimal

class ItemizedSaleFragment : androidx.fragment.app.Fragment() {

    private lateinit var totalValue: TextView
    private lateinit var currentSale: CurrentSale
    private lateinit var adapter: ItemizedSaleRecyclerViewAdapter
    private lateinit var payCashButton: Button
    private lateinit var payDebitButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalValue = view.findViewById(R.id.total)

        initItemizedSaleRecyclerView(view)
        observeCheckoutViewModel()
        initPayCashButton(view)
        initSwipeDebitButton(view)
        renderTotal()
    }

    private fun observeCheckoutViewModel() {
        currentSale.items.observe(this, Observer { items ->
            renderTotal()
            items?.let { adapter.setItems(it) }
        })
    }

    private fun initPayCashButton(view: View) {
        payCashButton = view.findViewById(R.id.payCashButton)
        payCashButton.setOnClickListener {
            Navigation.findNavController(this.activity!!, R.id.nav_host_fragment)
                .navigate(R.id.payCashFragment)
        }
    }

    private fun initSwipeDebitButton(view: View) {
        payDebitButton = view.findViewById(R.id.payDebitButton)
        payDebitButton.setOnClickListener {
            Navigation.findNavController(this.activity!!, R.id.nav_host_fragment)
                .navigate(R.id.swipeDebitFragment)
        }
    }

    private fun renderTotal() {
        val text = when {
            currentSale.total < BigDecimal.ZERO -> "Change Due: ${(currentSale.total.abs()).toCurrencyString()}"
            else -> "Total: ${currentSale.total.toCurrencyString()}"

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

    override fun onStart() {
        super.onStart()
        Navigation.findNavController(payCashButton).addOnDestinationChangedListener { _, destination, _ ->
            (destination.id == R.id.checkoutFragment).let { canCheckout ->
                payCashButton.isEnabled = canCheckout
                payDebitButton.isEnabled = canCheckout
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentSale = activity?.run {
            ViewModelProviders.of(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    private fun initItemizedSaleRecyclerView(view: View) {
        adapter = ItemizedSaleRecyclerViewAdapter()
        view.itemized_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.activity).apply {
            orientation = RecyclerView.VERTICAL
        }
        view.itemized_list.adapter = adapter
    }
}
