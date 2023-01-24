package org.lifetowncolumbus.pos.merchant.views.catalog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import org.lifetowncolumbus.pos.databinding.CatalogItemButtonBinding
import org.lifetowncolumbus.pos.merchant.models.CatalogItem

class CatalogGridAdapter internal constructor(context: Context, private val clickListener: (CatalogItem) -> Unit) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CatalogGridAdapter.CatalogItemViewHolder>() {

    private lateinit var binding: CatalogItemButtonBinding
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items = emptyList<CatalogItem>()

    inner class CatalogItemViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val catalogItemView: Button = binding.addSaleItem
        init {
            catalogItemView.setOnClickListener {
                clickListener(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogItemViewHolder {
        binding = CatalogItemButtonBinding.inflate(inflater, parent, false)
        return CatalogItemViewHolder(binding.root)
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