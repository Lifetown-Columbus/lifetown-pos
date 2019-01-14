package org.lifetowncolumbus.pos.merchant.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_itemized_sale.view.*
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.toCurrencyString
import java.math.BigDecimal

class ItemizedSaleFragment : Fragment() {

    private lateinit var totalValue: TextView
    private lateinit var currentSale: CurrentSale
    private lateinit var adapter: ItemizedSaleRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalValue = view.findViewById(R.id.total)

        initItemizedSaleRecyclerView(view)
        observeCheckoutViewModel()
        initPayCashButton(view)
        renderTotal()
    }

    private fun observeCheckoutViewModel() {
        currentSale.items.observe(this, Observer {
            renderTotal()
            adapter.loadItems(currentSale.items.value ?: emptyList())
            adapter.notifyDataSetChanged()
        })
    }

    private fun initPayCashButton(view: View) {
        val payCashButton: Button = view.findViewById(R.id.payCashButton)
        payCashButton.setOnClickListener {
            Navigation.findNavController(this.activity!!, R.id.nav_host_fragment)
                .navigate(R.id.payCashFragment)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentSale = activity?.run {
            ViewModelProviders.of(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")


    }

    private fun initItemizedSaleRecyclerView(view: View) {
        adapter = ItemizedSaleRecyclerViewAdapter()
        view.itemized_list.layoutManager = LinearLayoutManager(this.activity).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        view.itemized_list.adapter = adapter
    }
}
