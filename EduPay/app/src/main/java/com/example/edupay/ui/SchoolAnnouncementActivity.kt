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
import com.example.edupay.adapter.SchoolAnnouncementAdapter
import com.example.edupay.adapter.StudentActivityAdapter
import com.example.edupay.databinding.ActivityPaymentDetailsBinding
import com.example.edupay.databinding.ActivitySchoolAnnouncementBinding
import com.example.edupay.databinding.ActivityStudentActivityBinding
import com.example.edupay.databinding.OtpActivityBinding
import com.example.edupay.model.SchoolAnnouncement
import com.example.edupay.model.StudentEvent
import com.example.edupay.model.announcement.SchoolNotficationList
import com.example.edupay.model.parent_payment.PaymentListingData
import com.example.edupay.model.parent_register.Parent
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
class SchoolAnnouncementActivity : BaseActivity() {
    private var announcements= mutableListOf<SchoolNotficationList>()
    var myTinyDB: MyTinyDB? = null
    var payment_listing_data: String? = null
    var preferenceHelper: PreferenceHelper? = null
    private lateinit var binding: ActivitySchoolAnnouncementBinding
    private val viewModel: HomeViewModel by viewModels()
    private var parentData: Parent? = null
    private lateinit var adapter: AnnouncementAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.otp_activity)
        binding = ActivitySchoolAnnouncementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        parentData = myTinyDB!!.getParentData(Constants.PARENT_DATA)
        preferenceHelper = PreferenceHelper(this)
        preferenceHelper!!.save(Constants.NOTIFICATION_COUNT,0)
        if (intent != null) {
            if (intent.hasExtra(Constants.PAYMENT_LISTING_DATA)) {
                payment_listing_data = intent.getStringExtra(Constants.PAYMENT_LISTING_DATA)!!

            }
        }
        if(parentData!=null)
        {
            getSchoolAnnouncement(parentData!!.id)
        }
       // val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
       // binding.imageViewLogo.startAnimation(animation)

       /*  announcements = listOf(
            SchoolAnnouncement(
                R.drawable.ic_announcement,
                getString(R.string.announcement_title_bus_arriving),
                getString(R.string.announcement_msg_bus_arriving),
                R.color.green_500
            ),
            SchoolAnnouncement(
                R.drawable.ic_announcement,
                getString(R.string.announcement_title_emergency),
                getString(R.string.announcement_msg_emergency),
                R.color.dulex_warning
            ),
            SchoolAnnouncement(
                R.drawable.ic_payment,
                getString(R.string.announcement_title_payment),
                getString(R.string.announcement_msg_payment),
                R.color.primary_color
            )
            // Add more if needed
        )*/
        adapter = AnnouncementAdapter(announcements!!)
        binding.recyclerAnnouncements.layoutManager = LinearLayoutManager(this)
        binding.recyclerAnnouncements.adapter = adapter
     /*   binding.recyclerAnnouncements.apply {
            layoutManager = LinearLayoutManager(this@SchoolAnnouncementActivity)
            adapter = SchoolAnnouncementAdapter(announcements)
        }*/
    }

    private fun getSchoolAnnouncement(
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
        viewModel.getParentAnnouncement(parentId).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                it.data?.let { school ->
                    announcements.clear()
                    announcements.addAll(it.data)
                    binding.recyclerAnnouncements.scrollToPosition(0)
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