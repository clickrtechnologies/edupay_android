package com.example.edupay.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchableAdapter(
    private val originalItems: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SearchableAdapter.ViewHolder>(), Filterable {

    // Explicit type declarations
    private var filteredItems: List<String> = originalItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: String = filteredItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = filteredItems.size

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            originalItems
        } else {
            originalItems.filter { item: String ->
                item.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results: FilterResults = FilterResults()
            results.values = if (constraint.isNullOrEmpty()) {
                originalItems
            } else {
                originalItems.filter { item: String ->
                    item.contains(constraint.toString(), ignoreCase = true)
                }
            }
            results.count = (results.values as List<*>).size
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredItems = results?.values as? List<String> ?: emptyList()
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            val textView: TextView = itemView.findViewById(android.R.id.text1)
            textView.text = item
            itemView.setOnClickListener { onItemClick(item) }
        }
    }
}