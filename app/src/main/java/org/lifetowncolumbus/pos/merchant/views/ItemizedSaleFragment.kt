package org.lifetowncolumbus.pos.merchant.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_itemized_sale.view.*
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.viewModels.CashPayment
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.printing.OpenDrawerPrintJob
import org.lifetowncolumbus.pos.printing.PrintManager
import org.lifetowncolumbus.pos.toCurrencyString
import java.math.BigDecimal

class ItemizedSaleFragment : androidx.fragment.app.Fragment() {

    private lateinit var totalValue: TextView
    private lateinit var currentSale: CurrentSale
    private lateinit var adapter: ItemizedSaleRecyclerViewAdapter
    private lateinit var payCashButton: Button
    private lateinit var payCreditButton: Button
    private lateinit var quickCashButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalValue = view.findViewById(R.id.total)

        initItemizedSaleRecyclerView(view)
        observeCheckoutViewModel()
        initOpenDrawerButton(view)
        initPayCashButton(view)
        initPayCreditButton(view)
        initQuickCashButton(view)
        renderTotal()
    }

    private fun observeCheckoutViewModel() {
        currentSale.items.observe(this, Observer { items ->
            renderTotal()
            items?.let { adapter.setItems(it) }
        })

        currentSale.canCheckout.observe(this, Observer { canCheckout ->
            payCashButton.isEnabled = canCheckout
            payCreditButton.isEnabled = canCheckout
            quickCashButton.isEnabled = canCheckout
            adapter.setEnabled(canCheckout)
        })
    }

    private fun initQuickCashButton(view: View) {
        quickCashButton = view.findViewById(R.id.quickCashButton)
        quickCashButton.setOnClickListener {
            currentSale.payCash(CashPayment.worth(currentSale.total.toDouble()))
            Navigation.findNavController(this.activity!!, R.id.nav_host_fragment)
                .navigate(R.id.action_checkoutFragment_to_saleCompleteFragment)
        }

    }

    private fun initOpenDrawerButton(view: View) {
        (view.findViewById(R.id.openDrawerButton) as Button).setOnClickListener {
            PrintManager.print(OpenDrawerPrintJob());
        }
    }

    private fun initPayCashButton(view: View) {
        payCashButton = view.findViewById(R.id.payCashButton)
        payCashButton.setOnClickListener {
            Navigation.findNavController(this.activity!!, R.id.nav_host_fragment)
                .navigate(R.id.action_checkoutFragment_to_payCashFragment)
        }
    }

    private fun initPayCreditButton(view: View) {
        payCreditButton = view.findViewById(R.id.payCreditButton)
        payCreditButton.setOnClickListener {
            Navigation.findNavController(this.activity!!, R.id.nav_host_fragment)
                .navigate(R.id.action_checkoutFragment_to_swipeCreditFragment)
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
        Navigation.findNavController(this.requireView()).addOnDestinationChangedListener { _, destination, _ ->
            currentSale.currentDestination = destination.id
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentSale = activity?.run {
            ViewModelProvider(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    private fun initItemizedSaleRecyclerView(view: View) {
        adapter = ItemizedSaleRecyclerViewAdapter(currentSale::removeItem)
        view.itemized_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.activity).apply {
            orientation = RecyclerView.VERTICAL
        }
        view.itemized_list.adapter = adapter
    }
}
