package org.lifetowncolumbus.pos.merchant.views

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
//import kotlinx.android.synthetic.main.list_item.view.*
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.databinding.ListItemBinding
import org.lifetowncolumbus.pos.merchant.viewModels.Item
import org.lifetowncolumbus.pos.toCurrencyString


class ItemizedSaleRecyclerViewAdapter(private val removeHandler: (Int) -> Unit) : RecyclerView.Adapter<ItemizedSaleRecyclerViewAdapter.ListItemViewHolder>() {
    private var items: List<Item> = emptyList()
    private var enabled: Boolean = true

    fun setEnabled(isEnabled: Boolean) {
        enabled = isEnabled
        notifyDataSetChanged()
    }

    fun setItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.itemName.text = items[position].name
        holder.itemPrice.text = items[position].value.toCurrencyString()
        if (enabled) {
            holder.removeButton.isEnabled = true
            holder.removeButton.setOnClickListener { removeHandler(position) }
        } else {
            holder.removeButton.isEnabled = false
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ListItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val removeButton: Button = binding.removeItemButton
        val itemPrice: TextView = binding.itemPrice
        val itemName: TextView = binding.itemName
    }
}
