package com.example.edupay.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.model.school_register.School

class SearchableApiAdapter(
    private val originalItems: List<School>,
    private val onItemClick: (School) -> Unit
) : RecyclerView.Adapter<SearchableApiAdapter.ViewHolder>(), Filterable {

    private var filteredItems: List<School> = originalItems

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = filteredItems.size

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            originalItems
        } else {
            originalItems.filter { it.name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val resultList = if (constraint.isNullOrEmpty()) {
                originalItems
            } else {
                originalItems.filter {
                    it.name.contains(constraint.toString(), ignoreCase = true)
                }
            }

            return FilterResults().apply {
                values = resultList
                count = resultList.size
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredItems = results?.values as? List<School> ?: emptyList()
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: School) {
            val textView: TextView = itemView.findViewById(android.R.id.text1)
            textView.text = item.name
            itemView.setOnClickListener { onItemClick(item) }
        }
    }
}
