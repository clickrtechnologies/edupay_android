package com.example.edupay.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.edupay.fcm.AppUtils.isAppInForeground
import com.example.edupay.R
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.school.SchoolDashboardActivity
import com.example.edupay.ui.DetailActivity
import com.example.edupay.utils.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var desc: String?=""
    private var type: String?=""
    private var title: String?=""
    private var numMessages = 0
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
       // val notification = remoteMessage.notification
        val data = remoteMessage.data
      //  Log.d("FCM_TOKEN", "Refreshed token: $data")
      //  Toast.makeText(this,"Push Notification Received",Toast.LENGTH_LONG)
        if (data.containsKey("type")) {
             type = data["type"]
            Log.d("Check", "Type: $type")
        } else {
            Log.d("Check", "Key 'type' not found")
        }

        if (data.containsKey("descriptions")) {
             desc = data["descriptions"]
            Log.d("Check", "Description: $desc")
        }
        if (data.containsKey("title")) {
            title = data["title"]
            Log.d("Check", "Type: $title")
        }


     if (isAppInForeground(applicationContext)) {
            // Send broadcast
            val broadcastIntent = Intent(Constants.PUSH_NOTIFICATION_TAG)
            broadcastIntent.putExtra("title", title)
            broadcastIntent.putExtra("description", desc)
            broadcastIntent.putExtra(Constants.TYPE, type)
            sendBroadcast(broadcastIntent)
        } else {
            // App is in background, show normal notification
            sendNotification(data)

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun sendNotification(
        data: Map<String, String>
    ) {


        val intent = Intent(this, SchoolDashboardActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("descriptions", desc)
        intent.putExtra(Constants.TYPE, type)
        if(type.equals(Constants.EMERGENCY))
        {
            intent.putExtra(Constants.LOGIN_TYPE,  Constants.SIGNUP_TYPE_SCHOOL)
        }
        //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
       // with(this).save(Constants.IS_NOTIFICATION_COME, true)
        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val soundUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.notify)
        val notificationBuilder =
            NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setContentTitle(data["title"])
                .setContentText(data["descriptions"])
                .setAutoCancel(true)
                .setSound(soundUri)
               // .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                .setContentIntent(pendingIntent) // .setContentInfo("Hello")
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.logo
                    )
                ) //   .setColor(getColor(R.color.blue_text_color))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.logo)


        /*        try {
            String picture = data.get(FCM_PARAM);
            if (picture != null && !"".equals(picture)) {
                URL url = new URL(BuildConfig.CDN_URL+picture);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.notification_channel_id),
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESC
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(channel)
        }
        assert(notificationManager != null)
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", "Refreshed token: \$token")
        // Send this token to your server if needed
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d("FCM_TOKEN", "FIREBASE:Current token: $token")
        val preferenceHelper = PreferenceHelper(applicationContext)
        // Example: Save the token
        preferenceHelper.save(FIREBASE_TOKEN, token)
    }

    companion object {
        const val FCM_PARAM = "image"
        private const val CHANNEL_NAME = "FCM_BROWSER"
        private const val CHANNEL_DESC = "Firebase Cloud Messaging"
        const val FIREBASE_TOKEN = "token"
    }
}