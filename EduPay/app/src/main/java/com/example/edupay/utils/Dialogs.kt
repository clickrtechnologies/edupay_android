package com.example.edupay.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.*
import android.widget.*
import com.app.conatctsync.app.EduPay
import com.example.edupay.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


object Dialogs {
    public interface DialogCallback {
        fun ok()
        fun cancel() {
        }
    }

    public interface DialogSaveVideo {
        fun saveVideo(name: String, title: String)
    }

    const val SHORT_PERIOD = Toast.LENGTH_SHORT
    const val LONG_PERIOD = Toast.LENGTH_LONG

    var snackbar: Snackbar? = null

    enum class TIME {
        SHORT_PERIOD, LONG_PERIOD, INDEFINITE_PERIOD
    }

    @JvmStatic
    fun showMessagePrimaryColor(view: View?, context: Context, message: String?, time: Int) {
        snackbar = Snackbar.make(view!!, message!!, time)
        val sView = snackbar!!.view
        sView.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))

        // TextView sText = (TextView) sView.findViewById(android.support.design.R.id.snackbar_text);
        //sText.setTextColor(context.getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //  sText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        snackbar!!.show()
    }

    @JvmStatic
    fun show(msg: String) {
        Toast.makeText(EduPay.getInstance().applicationContext, msg, Toast.LENGTH_SHORT)
    }

    fun errorDialog(activity: Context, msg: String) {
        val materialAlertDialogBuilder: MaterialAlertDialogBuilder =
            MaterialAlertDialogBuilder(activity)
                .setTitle("Message")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(
                    activity.getString(R.string.close),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })

        materialAlertDialogBuilder.show()
    }

    fun errorDialogWithCallback(
        activity: Context,
        title: String,
        msg: String,
        btnText: String,
        isCancel: Boolean,
        dialogCallback: DialogCallback

    ) {
        val materialAlertDialogBuilder: MaterialAlertDialogBuilder =
            MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(
                    btnText,
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogCallback.ok()
                        dialogInterface.dismiss()
                    })

        if (isCancel) {
            materialAlertDialogBuilder.setNegativeButton(activity.getString(R.string.cancel), null)
        }

        materialAlertDialogBuilder.show()
    }

    fun updateAppDialog(activity: Context, title: String, msg: String) {
        val materialAlertDialogBuilder: MaterialAlertDialogBuilder =
            MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
        materialAlertDialogBuilder.show()
    }





/*    fun showProgressDialog(context: Activity, message: String): AlertDialog {
        val dialog = getAlertDialog(
            context,
            R.layout.custom_progressbar,
            setCancellationOnTouchOutside = false
        )
        dialog.show()
        val text_progress_bar = dialog.findViewById<TextView>(R.id.text_progress_bar)
        text_progress_bar.text = message
        return dialog
    }*/

    fun getAlertDialog(
        context: Activity,
        layout: Int,
        setCancellationOnTouchOutside: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val customLayout: View = context.layoutInflater.inflate(layout, null)
        builder.setView(customLayout)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(setCancellationOnTouchOutside)
        return dialog
    }

    lateinit var videoProgress: TextView


/*
    fun openInfoWindow(activity: Activity, sMapData: MapData): androidx.appcompat.app.AlertDialog {
        val builder = MaterialAlertDialogBuilder(
            activity,
            R.style.MyRounded_MaterialComponents_MaterialAlertDialog
        )
        //val builder = MaterialAlertDialogBuilder(activity)
        val infoWindow: View = activity.layoutInflater.inflate(R.layout.info_window_layout, null)

        val ivHeader = infoWindow.findViewById<ImageView>(R.id.imageView1) as ImageView
        val mWebView = infoWindow.findViewById<View>(R.id.webview) as WebView
        val tvTitle = infoWindow.findViewById<View>(R.id.tv_title) as TextView
        val tvType = infoWindow.findViewById<View>(R.id.tv_type) as TextView

        ivHeader.setOnClickListener {
            val intent = Intent(activity, FulleVideoImageActivity::class.java)
            intent.putExtra(Key.FILE_PATH, sMapData.filename)
            if (sMapData.file_type.equals("image")) {
                intent.putExtra(Key.IS_FILE_TYPE_VIDEO, false)
            }
            if (sMapData.file_type.equals("video")) {
                intent.putExtra(Key.IS_FILE_TYPE_VIDEO, true)
            }
            activity.startActivity(intent)
        }

        tvTitle.setText(sMapData.title);
        tvType.setText(sMapData.address);
        if (sMapData.file_type.equals("image") || sMapData.file_type.equals("video")) {
            ivHeader.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            Constants.loadImage(activity, sMapData.thumb, ivHeader);
        } else if (sMapData.file_type.equals("360")) {
            ivHeader.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);

            val url = sMapData.kuula_url
            val htmlString =
                "<html><body><iframe src=" + url + " width=100% height=100% playsinline=1  border=none frameborder=0 allow=accelerometer allowfullscreen=true webkitallowfullscreen=true mozallowfullscreen=true oallowfullscreen=true msallowfullscreen=true  allow=vr,gyroscope,accelerometer,fullscreen></iframe></body></html>"
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.loadData(htmlString, "text/html; charset=utf-8", null);
        } else {
            ivHeader.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
        }

        builder.setView(infoWindow)
        val dialog = builder.create()
        dialog.show()

        val window: Window? = dialog.getWindow()
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.TOP)

        val displayMetrics = DisplayMetrics()
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val displayHeight = displayMetrics.heightPixels
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        val dialogWindowWidth = (displayWidth * 0.6f).toInt()
        val dialogWindowHeight = (displayHeight * 0.5f).toInt()
        layoutParams.width = dialogWindowWidth
        layoutParams.height = dialogWindowHeight
        layoutParams.y = 110

        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 0, 0, 0, 0)
        //layoutParams.verticalMargin = 1f
        dialog.window!!.attributes = layoutParams
        val wmlp = dialog.window!!.attributes
        // dialog.window!!.setBackgroundDrawable(inset);


        return dialog
    }
*/



    fun dialogWithOption(activity: Context, msg: String, dialogCallback: DialogCallback) {
        val materialAlertDialogBuilder: MaterialAlertDialogBuilder =
            MaterialAlertDialogBuilder(activity)
                .setTitle(activity.getString(R.string.message))
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(
                    activity.getString(R.string.yes),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogCallback.ok()
                        dialogInterface.dismiss()
                    })
                .setNegativeButton(
                    activity.getString(R.string.no),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })

        materialAlertDialogBuilder.show()
    }

    fun dialogWithOption(
        activity: Context,
        title: String,
        msg: String,
        dialogCallback: DialogCallback
    ) {
        val materialAlertDialogBuilder: MaterialAlertDialogBuilder =
            MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(
                    activity.getString(R.string.confirm),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogCallback.ok()
                        dialogInterface.dismiss()
                    })
                .setNegativeButton(
                    activity.getString(R.string.cancel),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })

        materialAlertDialogBuilder.show()
    }

    fun dialogWithCloseButton(
        activity: Context,
        msg: String,
        btntext: String,
        cancelable: Boolean,
        dialogCallback: () -> Unit
    ) {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(activity)
            .setTitle(activity.getString(R.string.message))
            .setMessage(msg)
            .setCancelable(cancelable)
            .setPositiveButton(btntext) { dialogInterface, _ ->
                dialogInterface.dismiss()
                dialogCallback()
            }

        materialAlertDialogBuilder.show()
    }
 fun dialogWithCloseButton(
        activity: Context,
        title: String,
        msg: String,
        btntext: String,
        cancelable: Boolean
    ) {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(activity)
            .setTitle(activity.getString(R.string.message))
            .setMessage(msg)
            .setCancelable(cancelable)
            .setPositiveButton(btntext) { dialogInterface, _ ->
                dialogInterface.dismiss()

            }

        materialAlertDialogBuilder.show()
    }

    fun oldErrorDialog(activity: Context, msg: String) {
        var thisMsg = msg
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle(activity.getString(R.string.app_name))
        builder.setMessage(thisMsg)
            .setCancelable(false)
            .setPositiveButton(
                activity.getString(R.string.close),
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
        val alert: AlertDialog = builder.create()
        alert.show()
    }


}