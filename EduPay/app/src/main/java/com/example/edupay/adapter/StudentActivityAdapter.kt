package com.example.edupay.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.R
import com.example.edupay.databinding.ItemStudentActivityBinding
import com.example.edupay.model.StudentEvent
import com.example.edupay.model.student_activity.ActivityData

class StudentActivityAdapter(private val events: List<ActivityData>) :
    RecyclerView.Adapter<StudentActivityAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: ItemStudentActivityBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemStudentActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        with(holder.binding) {
            tvEventTitle.text = event.activityName
            tvEventDate.text = event.currentDate
            tvEventBrief.text = event.activityDescription
            tvParticipants.text = "Total No. Of Participants: ${event.totalNoOfParticipants}"
            tvVenue.text = "Venue: ${event.venue}"
            tvWinners.text = event.winners

            tvToggleDetails.setOnClickListener {
                if (detailsLayout.visibility == View.VISIBLE) {
                    detailsLayout.visibility = View.GONE
                    tvToggleDetails.text = root.context.getString(R.string.see_all_details)
                } else {
                    detailsLayout.visibility = View.VISIBLE
                    tvToggleDetails.text = root.context.getString(R.string.hide_details)
                }
            }
        }
    }

    override fun getItemCount(): Int = events.size
}
