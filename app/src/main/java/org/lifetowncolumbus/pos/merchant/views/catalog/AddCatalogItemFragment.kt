package org.lifetowncolumbus.pos.merchant.views.catalog


import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_add_catalog_item.view.*
import org.lifetowncolumbus.pos.KeyboardHelpers
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.models.CatalogItem
import org.lifetowncolumbus.pos.merchant.viewModels.Catalog


class AddCatalogItemFragment : androidx.fragment.app.Fragment() {
    private lateinit var catalog: Catalog
    private lateinit var catalogItem: CatalogItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val catalogId = arguments?.getLong("catalogItemId")

        if (catalogId != null && catalogId > 0) {
            // TODO catalogItem should have a view model that gets bound to this layout
            // the repository should be able to find it and do an upsert
//            catalogItem = catalog.getItem(catalogId)
        }

        view.saveCatalogItemButton.setOnClickListener {
            val itemName = view.catalogItemName.text.toString()
            val itemValue = view.catalogItemValue.text.toString().toDouble()
            // catalog should do an upsert here  ... if id null insert, else update
            catalog.addItem(CatalogItem(null, itemName, itemValue))
            Navigation.findNavController(view).navigate(R.id.action_addCatalogItemFragment_to_checkoutFragment)
            context?.let { context-> KeyboardHelpers.closeKeyboard(context, view) }
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
