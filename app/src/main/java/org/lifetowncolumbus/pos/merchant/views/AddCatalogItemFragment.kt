package org.lifetowncolumbus.pos.merchant.views


import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_add_catalog_item.view.*
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.models.CatalogItem
import org.lifetowncolumbus.pos.merchant.viewModels.Catalog


class AddCatalogItemFragment : Fragment() {
    private lateinit var catalog: Catalog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.saveCatalogItemButton.setOnClickListener {
            val itemName = view.catalogItemName.text.toString()
            val itemvalue = view.catalogItemValue.text.toString().toDouble()
            catalog.addItem(CatalogItem(null, itemName, itemvalue))
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
