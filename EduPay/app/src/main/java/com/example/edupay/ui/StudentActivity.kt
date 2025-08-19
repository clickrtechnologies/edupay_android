package com.example.edupay.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.adapter.AnnouncementAdapter
import com.example.edupay.adapter.StudentActivityAdapter
import com.example.edupay.databinding.ActivityPaymentDetailsBinding
import com.example.edupay.databinding.ActivityStudentActivityBinding
import com.example.edupay.databinding.OtpActivityBinding
import com.example.edupay.model.StudentEvent
import com.example.edupay.model.announcement.SchoolNotficationList
import com.example.edupay.model.parent_payment.PaymentListingData
import com.example.edupay.model.parent_register.Parent
import com.example.edupay.model.student_activity.ActivityData
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.school.PaymentActivity
import com.example.edupay.school.SchoolDashboardActivity
import com.example.edupay.school.SchoolProfileActivity
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentActivity : BaseActivity() {
    var myTinyDB: MyTinyDB? = null
    var payment_listing_data: String? = null
    var preferenceHelper: PreferenceHelper? = null
    private lateinit var binding: ActivityStudentActivityBinding
    private val viewModel: HomeViewModel by viewModels()
    private var announcements= mutableListOf<ActivityData>()
    private lateinit var adapter: StudentActivityAdapter
    private var parentData: Parent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.otp_activity)
        binding = ActivityStudentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        parentData = myTinyDB!!.getParentData(Constants.PARENT_DATA)

        if (intent != null) {
            if (intent.hasExtra(Constants.PAYMENT_LISTING_DATA)) {
                payment_listing_data = intent.getStringExtra(Constants.PAYMENT_LISTING_DATA)!!

            }
        }
        if(parentData!=null)
        {
            getStudentActivity(parentData!!.id)
        }
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.imageViewLogo.startAnimation(animation)

        val sampleEvents = listOf(
            StudentEvent(
                title = getString(R.string.event_title),
                date = getString(R.string.event_date),
                brief = getString(R.string.event_brief),
                participants = 20,
                venue = "Amphitheatre",
                winners = getString(R.string.event_winners)
            ),
            // Add more events if needed
        )

    /*    binding.recyclerViewActivities.apply {
            layoutManager = LinearLayoutManager(this@StudentActivity)
            adapter = StudentActivityAdapter(sampleEvents)
        }*/

        adapter = StudentActivityAdapter(announcements!!)
        binding.recyclerViewActivities.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewActivities.adapter = adapter
        /*   binding.recyclerAnnouncements.apply {
               layoutManager = LinearLayoutManager(this@SchoolAnnouncementActivity)
               adapter = SchoolAnnouncementAdapter(announcements)
           }*/

    }


    private fun getStudentActivity(
        parentId: Int,
    ) {

        if (!EduPay.hasNetwork()) {
            Dialogs.dialogWithCloseButton(
                activity = this,
                title = getString(R.string.network_title),
                msg = getString(R.string.network_error),
                btntext = getString(R.string.ok),
                cancelable = true
            )

        }
        showProgressDialog()
        viewModel.getStudentActivityAnnouncement(parentId).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                it.data?.let { school ->
                    announcements.clear()
                    announcements.addAll(it.data)
                    binding.recyclerViewActivities.scrollToPosition(0)
                    adapter.notifyDataSetChanged()

                }
            } else {
                if (!it.note.isNullOrEmpty()) {
                    Constants.showValidationDialog(this, it.note)
                }
            }

        })
    }



}