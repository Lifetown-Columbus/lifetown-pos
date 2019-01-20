package org.lifetowncolumbus.pos.merchant.views

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.viewModels.Catalog
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.merchant.viewModels.PurchasedItem
import org.lifetowncolumbus.pos.merchant.views.catalog.CatalogGridAdapter
import java.math.BigDecimal


class CheckoutFragment : androidx.fragment.app.Fragment() {
    private lateinit var currentSale: CurrentSale
    private lateinit var catalog: Catalog
    private lateinit var editCatalog: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editCatalog = view.editCatalogButton
        editCatalog.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_checkoutFragment_to_editCatalogFragment)
        }

        initCatalogView(view)
    }

    private fun initCatalogView(view: View) {
        val recyclerView = view.catalogRecyclerView
        val adapter = CatalogGridAdapter(this.activity!!) {
            val saleItem = PurchasedItem(BigDecimal.valueOf(it.value), it.name)
            currentSale.addItem(saleItem)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this.activity, 6)
        catalog.allItems.observe(this, Observer { items ->
            items?.let { adapter.setItems(it) }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            catalog = ViewModelProviders.of(this).get(Catalog::class.java)
            currentSale = ViewModelProviders.of(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}
