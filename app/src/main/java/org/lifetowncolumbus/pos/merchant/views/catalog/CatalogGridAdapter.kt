package org.lifetowncolumbus.pos.merchant.views.catalog

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.catalog_item_button.view.*
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.models.CatalogItem

class CatalogGridAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<CatalogGridAdapter.CatalogItemViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items = emptyList<CatalogItem>()

    inner class CatalogItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catalogItemView: Button = itemView.addSaleItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogItemViewHolder {
        val catalogItemButton = inflater.inflate(R.layout.catalog_item_button, parent, false)
        return CatalogItemViewHolder(catalogItemButton)
    }

    override fun onBindViewHolder(holder: CatalogItemViewHolder, position: Int) {
        val current = items[position]
        holder.catalogItemView.text = current.name
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<CatalogItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}