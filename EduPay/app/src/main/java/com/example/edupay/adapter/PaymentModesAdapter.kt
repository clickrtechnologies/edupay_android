package com.example.edupay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.R
import com.example.edupay.databinding.ItemPaymentModeBinding
import com.example.edupay.model.payment_method.PaymentOption

class PaymentModesAdapter(private val list: List<PaymentOption>?) :
    RecyclerView.Adapter<PaymentModesAdapter.PaymentModeViewHolder>() {

    inner class PaymentModeViewHolder(val binding: ItemPaymentModeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentModeViewHolder {
        val binding = ItemPaymentModeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentModeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentModeViewHolder, position: Int) {
        val item = list?.get(position)
        holder.binding.tvModeName.text = item?.paymenOption
        holder.binding.tvStatus.text = item?.status
     /*   holder.binding.tvStatus.setTextColor(
            ContextCompat.getColor(holder.itemView.context,
                if (item.isActive) R.color.green_500 else R.color.primary_color
            )
        )*/
    }

    override fun getItemCount(): Int
    {
        if(!list.isNullOrEmpty())
        {
            return list!!.size
        }
        return 0
    }
}
