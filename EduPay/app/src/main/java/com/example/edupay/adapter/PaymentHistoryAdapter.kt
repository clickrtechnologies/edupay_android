package com.example.edupay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.databinding.ItemPaymentHistoryCardBinding
import com.example.edupay.model.parent_payment.PaymentListingData

class PaymentHistoryAdapter(
    private val items: List<PaymentListingData>,
    private val onItemClick: (PaymentListingData) -> Unit
) : RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHistoryViewHolder>() {

    inner class PaymentHistoryViewHolder(private val binding: ItemPaymentHistoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PaymentListingData) {
            binding.tvChildName.text = model.studentName
            binding.tvTermPayment.text = model.term1Fee
            binding.tvInclusions.text = model.paymentDate
            binding.tvInclusions.text = model.status
            binding.tvAmount.text = model.amountPaid.toString()

            binding.root.setOnClickListener {
                onItemClick(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPaymentHistoryCardBinding.inflate(inflater, parent, false)
        return PaymentHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentHistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
