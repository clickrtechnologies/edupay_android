package com.example.edupay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.databinding.ItemAnnouncementBinding
import com.example.edupay.model.Announcement
import com.example.edupay.model.announcement.SchoolNotficationList

class AnnouncementAdapter(private val items: List<SchoolNotficationList>) :
    RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAnnouncementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SchoolNotficationList) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnnouncementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size
}
