package org.lifetowncolumbus.pos.merchant.views

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item.view.*
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.viewModels.Item
import org.lifetowncolumbus.pos.toCurrencyString


class ItemizedSaleRecyclerViewAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<ItemizedSaleRecyclerViewAdapter.ListItemViewHolder>() {
    private var items: List<Item> = emptyList()

    fun setItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.itemName.text = items[position].name
        holder.itemPrice.text = items[position].value.toCurrencyString()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ListItemViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val itemPrice: TextView = itemView.itemPrice
        val itemName: TextView = itemView.itemName
    }
}