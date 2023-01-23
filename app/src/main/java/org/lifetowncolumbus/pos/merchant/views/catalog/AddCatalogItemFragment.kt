package org.lifetowncolumbus.pos.merchant.views.catalog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.lifetowncolumbus.pos.KeyboardHelpers
import org.lifetowncolumbus.pos.databinding.FragmentAddCatalogItemBinding
import org.lifetowncolumbus.pos.merchant.models.CatalogItem
import org.lifetowncolumbus.pos.merchant.viewModels.Catalog


class AddCatalogItemFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentAddCatalogItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var catalog: Catalog
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val catalogItem = hydrateCatalogItem(arguments?.getLong("catalogItemId"), view)

        binding.saveCatalogItemButton.setOnClickListener {
            upsertCatalogItem(catalogItem, view)
        }

        binding.deleteCatalogItemButton.setOnClickListener {
            catalog.delete(catalogItem)
            navController.popBackStack()
        }

    }

    private fun upsertCatalogItem(
        catalogItem: CatalogItem,
        view: View
    ) {
        catalogItem.apply {
            name = binding.catalogItemName.text.toString()
            value = binding.catalogItemValue.text.toString().toDouble()
        }
        catalog.saveItem(catalogItem)
        navController.popBackStack()
        context?.let { context -> KeyboardHelpers.closeKeyboard(context, view) }
    }

    private fun hydrateCatalogItem(
        catalogId: Long?,
        view: View
    ): CatalogItem {
        val catalogItem = CatalogItem(null, "", 0.0)
        if (catalogId != null && catalogId > 0) {
            catalog.find(catalogId).observe(viewLifecycleOwner, Observer {
                catalogItem.id = it.id
                binding.catalogItemName.setText(it.name)
                binding.catalogItemValue.setText(it.value.toString())
            })
        }
        return catalogItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catalog = ViewModelProvider(this).get(Catalog::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCatalogItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
