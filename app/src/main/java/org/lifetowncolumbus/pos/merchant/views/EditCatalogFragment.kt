package org.lifetowncolumbus.pos.merchant.views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_edit_catalog.view.*
import org.lifetowncolumbus.pos.R


class EditCatalogFragment : Fragment() {

    private lateinit var addCatalogItem : Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addCatalogItem = view.addCatalogItemButton
        addCatalogItem.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_editCatalogFragment_to_addCatalogItemFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_catalog, container, false)
    }
}
