package com.example.edupay.utils

import android.content.Context
import com.example.edupay.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Constants {


    val ANNOUNCE: String?="announce"
    val PAYMENT: String?="payment"
    val EMERGENCY: String?="emergency"
    val SELECTED_LANGUAGE: String?="selected_language"
    val SPLASH: String?="splash"
    val PAYMENT_LISTING_DATA: String?="payment_listing_data"
    val IS_NOTIFICATION_COME: String="is_notification_come"
    val TYPE: String="type"
    val NOTIFICATION_COUNT: String="notification_count"
    val NOTIFICATION_COUNT_PAYMENT: String="notification_count_payment"
    val NOTIFICATION_COUNT_EMERGNECY: String="notification_count_emergnecy"
    val PUSH_NOTIFICATION_TAG: String="push_notification_tag"
    val FIREBASE_TOKEN: String="firebase_token"
    val PAY_STUDENT_DATA: String="PAY_STUDENT_DATA"
    val PAYMENT_DATA_REQUEST: String="PAYMENT_DATA_REQUEST"
    val SUCCESS="success"
    val AUTHORIZATION_HEADER="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWphQGdtYWlsLmNvbSIsImlhdCI6MTc0NzcyODA1NiwiZXhwIjoxNzQ3NzMxNjU2fQ.4bPfPc5pOH-sETn3zkUDH9kcSHtZYVS8IIi1p2U7mZ4"
    const val LOGIN_TYPE = "LOGIN_TYPE"
    const val LOGIN = "login"
    const val REG = "reg"
    const val child_detail = "child_detail"
    const val DASHBOARD = "dashboard"

    const val SCREEN_TYPE = "SCREEN_TYPE"
    const val SCREEN_TYPE_SIGN_UP = "screen_type_sign_up"
    const val SCREEN_TYPE_DASHBOARD = "screen_type_dashboard"
    const val LOGIN_TYPE_PARENT = "PARENT_LOGIN"
    const val SIGNUP_TYPE_PARENT = "PARENT_SIGNUP"
    const val LOGIN_TYPE_SCHOOL = "SCHOOL"
    const val SIGNUP_TYPE_SCHOOL = "SCHOOL_SIGNUP"
    const val EMAIL = "email"
    const val MOBILE = "mobile"
    const val COUNTRY_CODE = "countrycode"
    const val OTP = "otp"
    const val SCHOOL_DATA="SCHOOL_DATA"
    const val PARENT_DATA="PARENT_DATA"
    const val CHILD_DATA_LIST="child_data_list"
    const val PAYMENT_METHOD="payment_method"
    const val SCHOOL_LOGO="SCHOOL_LOGO"
    const val PARENT_LOGO="PARENT_LOGO"
    const val FATHER_NAME="FATHER_NAME"
    const val MOTHER_NAME="MOTHER_NAME"
    const val ADDITIONAL_EMAIL="ADDITIONAL_EMAIL"
    const val ACCESS_TOKEN="access_token"

     fun showValidationDialog(context: Context, message: String) {
        Dialogs.dialogWithCloseButton(
            activity = context,
            title = context.getString(R.string.message),
            msg = message,
            btntext =context. getString(R.string.ok),
            cancelable = false
        )

    }

    fun getCurrentDateFormatted(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
