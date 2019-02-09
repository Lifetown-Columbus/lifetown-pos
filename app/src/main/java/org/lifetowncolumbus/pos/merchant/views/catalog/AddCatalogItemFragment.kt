package org.lifetowncolumbus.pos.merchant.views.catalog


import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_add_catalog_item.view.*
import org.lifetowncolumbus.pos.KeyboardHelpers
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.models.CatalogItem
import org.lifetowncolumbus.pos.merchant.viewModels.Catalog


class AddCatalogItemFragment : androidx.fragment.app.Fragment() {
    private lateinit var catalog: Catalog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val catalogId = arguments?.getLong("catalogItemId")

        val catalogItem = CatalogItem(null, "", 0.0)

        if (catalogId != null && catalogId > 0) {
            catalog.find(catalogId).observe (this, Observer {
                catalogItem.id = it.id
                view.catalogItemName.setText(it.name.toString())
                view.catalogItemValue.setText(it.value.toString())
            })
        }

        view.saveCatalogItemButton.setOnClickListener {
            catalogItem.apply {
                name = view.catalogItemName.text.toString()
                value = view.catalogItemValue.text.toString().toDouble()
            }
            catalog.saveItem(catalogItem)
            Navigation.findNavController(view).navigate(R.id.checkoutFragment)
            context?.let { context-> KeyboardHelpers.closeKeyboard(context, view) }
        }

        view.deleteCatalogItemButton.setOnClickListener {
            catalog.delete(catalogItem)
            Navigation.findNavController(view).navigate(R.id.checkoutFragment)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catalog = ViewModelProviders.of(this).get(Catalog::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_catalog_item, container, false)
    }
}
