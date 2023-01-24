package org.lifetowncolumbus.pos.merchant.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.databinding.FragmentCheckoutBinding
import org.lifetowncolumbus.pos.merchant.viewModels.Catalog
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.merchant.viewModels.PurchasedItem
import org.lifetowncolumbus.pos.merchant.views.catalog.CatalogGridAdapter
import java.math.BigDecimal

class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentSale: CurrentSale
    private lateinit var catalog: Catalog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCatalogView(view)
    }

    private fun initCatalogView(view: View) {
        val recyclerView = binding.catalogRecyclerView
        val adapter = CatalogGridAdapter(this.requireActivity()) {
            val saleItem = PurchasedItem(BigDecimal.valueOf(it.value), it.name)
            currentSale.addItem(saleItem)
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
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            catalog = ViewModelProvider(this).get(Catalog::class.java)
            currentSale = ViewModelProvider(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
