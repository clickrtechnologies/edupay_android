package com.example.edupay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.databinding.ItemSchoolAnnouncementBinding
import com.example.edupay.model.SchoolAnnouncement

class SchoolAnnouncementAdapter(private val items: List<SchoolAnnouncement>) :
    RecyclerView.Adapter<SchoolAnnouncementAdapter.AnnouncementViewHolder>() {

    inner class AnnouncementViewHolder(val binding: ItemSchoolAnnouncementBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val binding = ItemSchoolAnnouncementBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            iconImage.setImageResource(item.iconResId)
            tvTitle.text = item.title
            tvMessage.text = item.message
            tvTitle.setTextColor(ContextCompat.getColor(root.context, item.titleColor))
        }
    }

    override fun getItemCount(): Int = items.size
}
