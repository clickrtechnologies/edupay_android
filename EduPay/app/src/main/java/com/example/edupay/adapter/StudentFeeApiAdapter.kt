package com.example.edupay.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.model.regstudents.StudentReg
import com.example.edupay.model.school_register.School

class StudentFeeApiAdapter(
    private val originalItems: List<StudentReg>,
    private val onItemClick: (StudentReg) -> Unit
) : RecyclerView.Adapter<StudentFeeApiAdapter.ViewHolder>(), Filterable {

    private var filteredItems: List<StudentReg> = originalItems

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
            originalItems.filter { it.studentName.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val resultList = if (constraint.isNullOrEmpty()) {
                originalItems
            } else {
                originalItems.filter {
                    it.studentName.contains(constraint.toString(), ignoreCase = true)
                }
            }

            return FilterResults().apply {
                values = resultList
                count = resultList.size
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredItems = results?.values as? List<StudentReg> ?: emptyList()
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: StudentReg) {
            val textView: TextView = itemView.findViewById(android.R.id.text1)
            textView.text = item.studentName
            itemView.setOnClickListener { onItemClick(item) }
        }
    }
}
