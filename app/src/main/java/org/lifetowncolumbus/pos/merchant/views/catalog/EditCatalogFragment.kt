package org.lifetowncolumbus.pos.merchant.views.catalog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_edit_catalog.view.*
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.viewModels.Catalog


class EditCatalogFragment : androidx.fragment.app.Fragment() {

    private lateinit var addCatalogItem : Button
    private lateinit var catalog: Catalog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addCatalogItem = view.addCatalogItemButton
        addCatalogItem.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_editCatalogFragment_to_addCatalogItemFragment)
        }

        initEditCatalogView(view)
    }

    private fun initEditCatalogView(view: View) {
        val recyclerView = view.editCatalogRecyclerView

        val adapter = CatalogGridAdapter(this.activity!!) {
            val bundle = Bundle()
            if(it.id != null) {
                bundle.putLong("catalogItemId", it.id!!)
                Navigation.findNavController(view).navigate(R.id.action_editCatalogFragment_to_addCatalogItemFragment, bundle)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this.activity, 6)
        catalog.allItems.observe(viewLifecycleOwner, Observer { items ->
            items?.let { adapter.setItems(it) }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_catalog, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            catalog = ViewModelProvider(this).get(Catalog::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}
