package com.example.edupay.school

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.ui.DetailActivity
import com.example.edupay.R
import com.example.edupay.ui.TransportationActivity
import com.example.edupay.ui.UnderDevelopment
import com.example.edupay.adapter.DashboardAdapter
import com.example.edupay.databinding.ActivitySchoolDashboardBinding
import com.example.edupay.listener.OnFieldSelection
import com.example.edupay.model.DashboardOption
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.ui.BaseActivity
import com.example.edupay.ui.LanguageSelectionActivity
import com.example.edupay.ui.LoginTypeActivity
import com.example.edupay.ui.PaymentHistoryActivity
import com.example.edupay.ui.SchoolAnnouncementActivity
import com.example.edupay.ui.StudentActivity
import com.example.edupay.utils.Constants
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchoolDashboardActivity : BaseActivity() {
    private var adapter: DashboardAdapter? = null
    private var options: List<DashboardOption>? = null
    var LOGIN_TYPE: String = Constants.LOGIN_TYPE_PARENT
    var myTinyDB: MyTinyDB? = null
    private val viewModel: HomeViewModel by viewModels()
    var preferenceHelper: PreferenceHelper? = null
    private lateinit var binding: ActivitySchoolDashboardBinding


    // Launcher for the permission request
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted – proceed with showing notifications
        } else {
            // Permission denied – show rationale or handle accordingly
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

        binding = ActivitySchoolDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myTinyDB = MyTinyDB(this)
        preferenceHelper = PreferenceHelper(this)

        if (intent != null) {
            if (intent.hasExtra(Constants.LOGIN_TYPE)) {
                LOGIN_TYPE = intent.getStringExtra(Constants.LOGIN_TYPE)!!
            }

        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(
                    "FCM_TOKEN",
                    "Fetching FCM registration token failed", task.exception
                )
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("FCM_TOKEN", "Current token: $token")
            // Example: Save the token
            preferenceHelper!!.save(Constants.FIREBASE_TOKEN, token)

            // Send token to your server if needed
            // sendTokenToServer(token)
        }
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        //   binding.ivLogo.startAnimation(animation)

        checkAndRequestNotificationPermission()
        if (LOGIN_TYPE == Constants.SIGNUP_TYPE_SCHOOL) {
            binding.tvDashboardTitle.text = getString(R.string.school_dashboard)
            var notficationCount = preferenceHelper!!.getValue(
                Constants.NOTIFICATION_COUNT_EMERGNECY,
                0
            )
            options = listOf(
                DashboardOption(
                    getString(R.string.school_announcements),
                    R.drawable.ic_announcement, 0,
                    AnnouncementsActivity::class.java
                ),
                DashboardOption(
                    getString(R.string.payment_notifications),
                    R.drawable.ic_payment_history, 0,
                    PaymentNotificationActivity::class.java
                ),
                DashboardOption(
                    getString(R.string.payment_statistics),
                    R.drawable.ic_stats, 0,
                    PaymentStatsActivity::class.java
                ),
                DashboardOption(
                    getString(R.string.paid_vs_unpaid),
                    R.drawable.ic_payment_icon, 0,
                    PaidUnpaidActivity::class.java
                ),
                DashboardOption(
                    getString(R.string.edit_profile),
                    R.drawable.ic_activity, 0,
                    SchoolProfileActivity::class.java
                ),
                DashboardOption(
                    getString(R.string.payment_modes),
                    R.drawable.ic_payment_modes, 0,
                    PaymentModesActivity::class.java
                ),
                DashboardOption(
                    getString(R.string.emergency_notification),
                    R.drawable.ic_notifications, notficationCount,
                    EmergencyActivity::class.java
                ), DashboardOption(
                    getString(R.string.select_language),
                    R.drawable.ic_langauage, 0,
                    LanguageSelectionActivity::class.java
                ),
                DashboardOption(
                    "logout",
                    R.drawable.ic_logout_icon, 0,
                    UnderDevelopment::class.java
                )
            )

        } else {
            binding.tvDashboardTitle.text = getString(R.string.parent_dashboard)
            val anoouncementCount = preferenceHelper!!.getValue(
                Constants.NOTIFICATION_COUNT, 0
            )
            options = listOf(
                DashboardOption(
                    getString(R.string.payment),
                    R.drawable.ic_payment_icon, 0,
                    DetailActivity::class.java
                ),
                DashboardOption(
                    getString(R.string.bus_tracking),
                    R.drawable.ic_bus, 0,
                    TransportationActivity::class.java
                ),
                DashboardOption(
                    getString(R.string.school_announcements),
                    R.drawable.ic_announce, anoouncementCount,
                    SchoolAnnouncementActivity::class.java
                ),
                DashboardOption(
                    getString(R.string.payment_history),
                    R.drawable.ic_payment_history, 0,
                    PaymentHistoryActivity::class.java
                ),
                DashboardOption(
                    getString(R.string.student_activity),
                    R.drawable.ic_activity, 0,
                    StudentActivity::class.java
                ),
                DashboardOption(
                    getString(R.string.select_language),
                    R.drawable.ic_langauage, 0,
                    LanguageSelectionActivity::class.java
                ),
                DashboardOption(
                    "logout",
                    R.drawable.ic_logout_icon, 0,
                    UnderDevelopment::class.java
                )
            )
        }

        adapter = DashboardAdapter(options!!, this, object : OnFieldSelection {
            override fun onLogoutClick() {
                showLogoutDialog()
            }

            override fun onOptionSelection(items: List<DashboardOption>, position: Int) {
                options!!.get(position).isNotify = 0
                if (LOGIN_TYPE == Constants.SIGNUP_TYPE_SCHOOL) {
                    if (position == 6) {
                        options!!.get(position).isNotify = 0
                        preferenceHelper!!.save(Constants.NOTIFICATION_COUNT_EMERGNECY, 0)
                        adapter!!.setFieldData(options!!)
                    }
                } else {
                    if (position == 2) {
                        options!!.get(2).isNotify = 0
                        preferenceHelper!!.save(
                            Constants.NOTIFICATION_COUNT, 0
                        )
                        adapter!!.setFieldData(options!!)

                    }

                }
            }
        })

        binding.recyclerDashboard.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerDashboard.adapter = adapter


        onNotificationReceived()
        // MARK: - Take notification intent
        notificationIntent()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.logout_title))
            .setMessage(getString(R.string.logout_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                myTinyDB?.clear()
                callFutherSCreen()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }

    private fun callFutherSCreen() {
        val LOGIN_TYPE = preferenceHelper!!.getValue(Constants.LOGIN_TYPE, "")

        when (LOGIN_TYPE) {
            Constants.SIGNUP_TYPE_SCHOOL -> {
                preferenceHelper!!.save(Constants.LOGIN_TYPE, "")
                val intent = Intent(this, LoginTypeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finishAffinity()
            }

            Constants.SIGNUP_TYPE_PARENT -> {
                preferenceHelper!!.save(Constants.LOGIN_TYPE, "")
                val intent = Intent(this, LoginTypeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finishAffinity()

            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission already granted
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                // Show a rationale to the user, if needed
                // Then request permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // Directly request the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private var mReceiver: BroadcastReceiver? = null

    // MARK: -recived all forground notification here
    fun onNotificationReceived() {
        val intentFilter = IntentFilter(Constants.PUSH_NOTIFICATION_TAG)
        mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val bundle = intent.extras
                if (bundle != null) {
                    if (bundle.containsKey(Constants.TYPE)) {
                        val notify = bundle.getString(Constants.TYPE)
                        var notficationCount =
                            preferenceHelper!!.getValue(Constants.NOTIFICATION_COUNT, 0)
                        notficationCount = notficationCount + 1
                        if (notify != null) {
                            if (LOGIN_TYPE == Constants.SIGNUP_TYPE_PARENT) {
                                if (notify.equals(
                                        Constants.ANNOUNCE,
                                        ignoreCase = true
                                    ) || notify.equals(Constants.PAYMENT, ignoreCase = true)
                                ) {
                                    println("NOTFICATION_TYPE:::" + Constants.ANNOUNCE)
                                    options!!.get(2).isNotify = notficationCount
                                    preferenceHelper!!.save(
                                        Constants.NOTIFICATION_COUNT,
                                        notficationCount
                                    )
                                    adapter!!.setFieldData(options!!)
                                }
                            } else if (LOGIN_TYPE == Constants.SIGNUP_TYPE_SCHOOL) {
                                var notficationCount = preferenceHelper!!.getValue(
                                    Constants.NOTIFICATION_COUNT_EMERGNECY,
                                    0
                                )
                                notficationCount = notficationCount + 1
                                println("NOTFICATION_TYPE:::" + Constants.EMERGENCY)
                                if (notify.equals(Constants.EMERGENCY, ignoreCase = true)) {
                                    println("NOTFICATION_TYPE:::" + Constants.EMERGENCY)
                                    options!!.get(6).isNotify = notficationCount
                                    preferenceHelper!!.save(
                                        Constants.NOTIFICATION_COUNT_EMERGNECY,
                                        notficationCount
                                    )
                                    adapter!!.setFieldData(options!!)
                                }
                            }

                        }

                    }
                }
            }
        }
        // this.registerReceiver(mReceiver, intentFilter)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(mReceiver, intentFilter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mReceiver != null) {
            unregisterReceiver(mReceiver)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        println("NOTFICATION_TYPE:::" + intent.getStringExtra(Constants.TYPE))
        Log.e("onNewIntent", "onNewIntent")

        //  Branch.getInstance().reInitSession(this, branchReferralInitListener)
    }

    // MARK: -Received all Background notification here

    fun notificationIntent() {
        if (intent.hasExtra(Constants.TYPE)) {
            val notify = intent.getStringExtra(Constants.TYPE)

            if (notify != null) {
                if (notify.equals(Constants.ANNOUNCE, ignoreCase = true)) {
                    println("NOTFICATION_TYPE:::" + Constants.ANNOUNCE)
                    val loginIntent =
                        Intent(this@SchoolDashboardActivity, SchoolAnnouncementActivity::class.java)
                    // loginIntent.putExtras(bundle)
                    startActivity(loginIntent)
                } else if (notify.equals(Constants.PAYMENT, ignoreCase = true)) {
                    println("NOTFICATION_TYPE:::" + Constants.PAYMENT)
                    val loginIntent =
                        Intent(this@SchoolDashboardActivity, SchoolAnnouncementActivity::class.java)
                    // loginIntent.putExtras(bundle)
                    startActivity(loginIntent)
                } else if (notify.equals(Constants.EMERGENCY, ignoreCase = true)) {

                    if (LOGIN_TYPE == Constants.SIGNUP_TYPE_SCHOOL) {
                        println("NOTFICATION_TYPE:::" + Constants.EMERGENCY)
                        val loginIntent =
                            Intent(this@SchoolDashboardActivity, EmergencyActivity::class.java)
                        // loginIntent.putExtras(bundle)
                        startActivity(loginIntent)
                    }

                }
            }


        }
    }

}