package com.example.edupay.school

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.conatctsync.app.EduPay
import com.example.edupay.ui.BaseActivity
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.adapter.PaymentNotificationAdapter
import com.example.edupay.databinding.ActivityPaymentInfoBinding
import com.example.edupay.model.payment_notfication.PaymentNotiData
import com.example.edupay.model.payment_notfication.PaymentNotificationRequest
import com.example.edupay.model.school_register.School
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentNotificationActivity : BaseActivity() {
    private var schoolData: School? = null

    private lateinit var binding: ActivityPaymentInfoBinding
    private lateinit var adapter: PaymentNotificationAdapter
    private val notifications = mutableListOf<PaymentNotiData>()
    var myTinyDB: MyTinyDB? = null
    private val viewModel: HomeViewModel by viewModels()
    var preferenceHelper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)
        schoolData = myTinyDB?.getSchoolData(Constants.SCHOOL_DATA)
        adapter = PaymentNotificationAdapter(notifications)
        binding.rvNotifications.layoutManager = LinearLayoutManager(this)
        binding.rvNotifications.adapter = adapter

        binding.btnAdd.setOnClickListener {
            showNewNotificationDialog()
        }
        if(schoolData!=null)
        {
            getPaymentNotification(schoolData!!.id)
        }
    }

    private fun showNewNotificationDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_new_payment_notification, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etMessage = dialogView.findViewById<EditText>(R.id.etMessage)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinnerAudience)
        val btnSend = dialogView.findViewById<Button>(R.id.btnSend)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnSend.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val message = etMessage.text.toString().trim()
            val audience = spinner.selectedItem.toString()

            if (title.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val request =
                PaymentNotificationRequest(
                    audience,
                    message,
                    schoolData!!.id,
                    title,
                )

            createPaymentNotification(request)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun createPaymentNotification(request: PaymentNotificationRequest) {

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
        val currentDate = Constants.getCurrentDateFormatted()

        viewModel.createPaymentNotification(request).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                it.school?.let { school ->
                    val newNotification = PaymentNotiData(
                        branch = school.branch,
                        id = school.id,
                        message = school.message,
                        schoolId = schoolData!!.id,
                        title = school.title
                    )
                    notifications.add(0, newNotification)
                    adapter.notifyItemInserted(0)
                    binding.rvNotifications.scrollToPosition(0)
                }
            } else {
                if (!it.note.isNullOrEmpty()) {
                    Constants.showValidationDialog(this, it.note)
                }
            }

        })
    }

    private fun getPaymentNotification(
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
        viewModel.getPaymentNotification(schooldID).observe(this, Observer {
            dismissProgressDialog()
            if (it.message.equals(Constants.SUCCESS)) {

                it.school?.let { school ->
                    notifications.clear()
                    notifications.addAll(it.school)
                    binding.rvNotifications.scrollToPosition(0)
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