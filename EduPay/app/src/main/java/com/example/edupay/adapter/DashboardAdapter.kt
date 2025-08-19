package com.example.edupay.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.R
import com.example.edupay.databinding.ItemDashboardCardBinding
import com.example.edupay.listener.OnFieldSelection
import com.example.edupay.model.DashboardOption
import com.example.edupay.utils.Constants

class DashboardAdapter(
    private var items: List<DashboardOption>,
    private val context: Context,
    private val onFieldSelection: OnFieldSelection
) :
    RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemDashboardCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(option: DashboardOption) {
            binding.ivIcon.setImageResource(option.iconRes)
            if (items.get(position).title.equals("logout", ignoreCase = true)) {
                binding.tvTitle.text = context.getString(R.string.logout)
            } else {
                binding.tvTitle.text = option.title
            }

            if (items.get(position).isNotify > 0) {
                binding.circleText.visibility = View.VISIBLE
                binding.circleText.text = items.get(position).isNotify.toString()
            } else {
                binding.circleText.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                val position = adapterPosition
                println("titleValue:::" + items.get(position).title)
                if (items.get(position).title.equals("logout", ignoreCase = true)) {
                    onFieldSelection.onLogoutClick()
                } else {

                    if (position != RecyclerView.NO_POSITION) {
                        onFieldSelection.onOptionSelection(items, position)
                        val intent = Intent(context, items[position].destination)
                        intent.putExtra(Constants.SCREEN_TYPE, Constants.SCREEN_TYPE_DASHBOARD)
                        context.startActivity(intent)

                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDashboardCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size
    fun setFieldData(options: List<DashboardOption>) {
        this.items = options
        notifyDataSetChanged()

    }


}
