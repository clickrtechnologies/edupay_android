package com.example.edupay.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.databinding.ItemStudentPaymentBinding

import com.example.edupay.model.StudentChart
import com.example.edupay.model.paid_unpaid.Payment
import com.example.edupay.model.school_stats.Student

class StudentPaymentAdapter(private var list: ArrayList<Student>?) :
    RecyclerView.Adapter<StudentPaymentAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemStudentPaymentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentPaymentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (list != null) {
            val student = list!![position]
            with(holder.binding) {
                tvName.text = student.studentName
                tvStatus.text = student.payment
                tvStatus.setTextColor(if (student.payment.equals("Paid")) Color.GREEN else Color.RED)
                tvMode.text = "Mode: ${student.paymentMode ?: "N/A"}"
            }
        }

    }

    override fun getItemCount(): Int {
        if (list != null) {
            return list!!.size
        } else
            return 0

    }

    fun updateData(newList: List<Student>) {
        if (list == null) {
            list = ArrayList()
        }
        list!!.clear()
        list!!.addAll(newList)
        notifyDataSetChanged()
    }
}
