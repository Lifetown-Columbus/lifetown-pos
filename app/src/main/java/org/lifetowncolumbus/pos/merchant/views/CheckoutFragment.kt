package org.lifetowncolumbus.pos.merchant.views

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_checkout.view.*
import org.lifetowncolumbus.pos.KeyboardHelpers
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.viewModels.Catalog
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.merchant.viewModels.PurchasedItem
import java.lang.Exception
import java.math.BigDecimal


class CheckoutFragment : Fragment() {
    private lateinit var currentSale: CurrentSale
    private lateinit var catalog: Catalog
    private lateinit var itemValue: EditText
    private lateinit var addItemButton: Button
    private lateinit var editCatalog: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemValue = view.itemValue
        addItemButton = view.addItemButton
        editCatalog = view.editCatalogButton

        addItemButton.setOnClickListener { addItem(it) }

        editCatalog.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_checkoutFragment_to_editCatalogFragment)
        }

        itemValue.setOnEditorActionListener { _, actionId, _ ->
            KeyboardHelpers.clickButtonWhenKeyboardDone(actionId, addItemButton)
        }

        initCatalogView(view)
    }

    private fun initCatalogView(view: View) {
        val recyclerView = view.catalogRecyclerView
        val adapter = CatalogGridAdapter(this.activity!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this.activity, 6)
        catalog.allItems.observe(this, Observer { items ->
            items?.let { adapter.setItems(it) }
        })
    }

    private fun addItem(view: View) {
        val amount = itemValue.text.toString().toDoubleOrNull()
        if (amount != null) currentSale.addItem(
            PurchasedItem(
                BigDecimal.valueOf(amount)
            )
        )

        itemValue.apply { text = null }
        KeyboardHelpers.closeKeyboard(context!!, view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catalog = ViewModelProviders.of(this).get(Catalog::class.java)
        currentSale = activity?.run {
            ViewModelProviders.of(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}
