package com.example.edupay.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.R
import com.example.edupay.databinding.ItemStudentStatusBinding
import com.example.edupay.model.PaymentStatus
import com.example.edupay.model.Student
import com.example.edupay.model.StudentChart
import com.example.edupay.model.paid_unpaid.Payment

class StudentAdapter(private var students: ArrayList<Payment>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvMode: TextView = itemView.findViewById(R.id.tvMode)
        val tvTerm: TextView = itemView.findViewById(R.id.tvTerm)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_status, parent, false)
        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        val context = holder.itemView.context
        holder.tvName.text = student.studentName
        holder.tvStatus.text = if (student.status.equals("Paid")) {
            context.getString(R.string.paid)
        } else {
            context.getString(R.string.unpaid)
        }
        holder.tvStatus.setTextColor(
            if (student.status.equals("Paid")) Color.parseColor("#4CAF50") else Color.RED
        )
        holder.tvMode.text = if (student.status.equals("Paid")) {
            context.getString(R.string.mode_with_value, student.paymentMethod)
        } else {
            context.getString(R.string.mode_na)
        }
        //  holder.tvTerm.text = context.getString(R.string.term_with_value, student.term.toString())    }
    }
    fun updateData(newList: ArrayList<Payment>) {
        students.clear()
        students = newList
        notifyDataSetChanged()
    }
}


