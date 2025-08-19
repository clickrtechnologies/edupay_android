package com.example.edupay.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.databinding.ItemNotificationBinding
import com.example.edupay.model.payment_notfication.PaymentNotiData
import java.text.SimpleDateFormat
import java.util.*

class PaymentNotificationAdapter(
    private val list: List<PaymentNotiData>
) : RecyclerView.Adapter<PaymentNotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaymentNotiData) {
            binding.tvTitle.text = item.title
            binding.tvMessage.text = item.message
            binding.tvAudience.text = "To: ${item.branch}"
           // binding.tvTime.text = formatDate(item.time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    private fun formatDate(timeMillis: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return sdf.format(Date(timeMillis))
    }
}
