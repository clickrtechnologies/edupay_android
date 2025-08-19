package com.example.edupay.school

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.conatctsync.app.EduPay
import com.example.edupay.ui.BaseActivity
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.adapter.AnnouncementAdapter
import com.example.edupay.databinding.ActivityAnnouncementsBinding
import com.example.edupay.databinding.ActivityEmergencyBinding
import com.example.edupay.model.announcement.CreateAnnounceRequest
import com.example.edupay.model.announcement.SchoolNotficationList
import com.example.edupay.model.school_register.School
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmergencyActivity : BaseActivity() {
    private var schoolData: School? = null

    private lateinit var binding: ActivityEmergencyBinding
    private val announcements = mutableListOf<SchoolNotficationList>()
    private lateinit var adapter: AnnouncementAdapter
    var myTinyDB: MyTinyDB? = null
    private val viewModel: HomeViewModel by viewModels()
    var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        schoolData = myTinyDB?.getSchoolData(Constants.SCHOOL_DATA)
        adapter = AnnouncementAdapter(announcements)
        binding.rvAnnouncements.layoutManager = LinearLayoutManager(this)
        binding.rvAnnouncements.adapter = adapter

        if(schoolData!=null)
        {
            getEmergencyNotification(schoolData!!.id)
        }

    }




    private fun getEmergencyNotification(
        schooldID: Int,
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
        viewModel.getEmergencyNotification(schooldID).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                it.data?.let { school ->
                    announcements.clear()
                    announcements.addAll(it.data)
                    binding.rvAnnouncements.scrollToPosition(0)
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


