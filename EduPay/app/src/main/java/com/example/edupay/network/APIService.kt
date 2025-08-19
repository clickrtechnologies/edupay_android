package com.example.edupay.network

import com.example.edupay.model.announcement.CreateAnnounceRequest
import com.example.edupay.model.announcement.ParentAnnouncementListing
import com.example.edupay.model.announcement.ResponseAnnouncementListing
import com.example.edupay.model.announcement.ResponseEmergencyListing
import com.example.edupay.model.announcement.ResponseSchoolAnnouncement
import com.example.edupay.model.branches.BranchesResponse
import com.example.edupay.model.paid_unpaid.ResponsePaidUnpaid
import com.example.edupay.model.parent_payment.PaymentListingResponse
import com.example.edupay.model.parent_register.ParentResponse
import com.example.edupay.model.parent_register.UpdateParentDetail
import com.example.edupay.model.payment.PaymentParentRequest
import com.example.edupay.model.payment.PaymentResponsess
import com.example.edupay.model.payment.partialPayment.PartialPaymentRequest
import com.example.edupay.model.payment_method.PaymentMethodResponse
import com.example.edupay.model.payment_notfication.PaymentNotificationRequest
import com.example.edupay.model.payment_notfication.ResponsePaymentNotifcation
import com.example.edupay.model.payment_notfication.ResponseSchoolNotification
import com.example.edupay.model.payment_selection.SchoolDetailsResponse
import com.example.edupay.model.regstudents.ParentIdRequest
import com.example.edupay.model.regstudents.StudentListResponse
import com.example.edupay.model.school_register.RegisterSchoolRequest
import com.example.edupay.model.school_register.RegisteredSchoolListResponse
import com.example.edupay.model.school_register.RegisteredSchoolResponse
import com.example.edupay.model.school_register.UpdateSchoolRequest
import com.example.edupay.model.school_stats.ResponseReceivedStats
import com.example.edupay.model.school_stats.due.ResponseDueStatus
import com.example.edupay.model.sessions.SessionsResponse
import com.example.edupay.model.student_activity.StudentActivityResponse
import com.example.edupay.model.student_fees_data.VerifyStudentFeesResponse
import com.example.edupay.model.upload_logo.UploadLogoResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by devanshramen on 11/19/17.
 */
interface APIService {
    //  -----------------------------------Login----------------------------------------//
    @POST("school/register")
    fun registeredSchool(@Body request: RegisterSchoolRequest): Observable<RegisteredSchoolResponse>

    @POST("parent/register")
    fun registeredParent(@Body request: RegisterSchoolRequest): Observable<ParentResponse>

    @PATCH("parent/{id}")
    fun updateParentInfo(
        @Path("id") schoolId: Int,
        @Body request: UpdateParentDetail,
        @Header("Authorization") token: String
    ): Observable<ParentResponse>

    @POST("school/login")
    fun loginSchool(@Body request: RegisterSchoolRequest): Observable<RegisteredSchoolResponse>

    @POST("school/resendOtp")
    fun resendOtpSchool(
        @Body request: RegisterSchoolRequest,
        @Header("Authorization") token: String
    ): Observable<RegisteredSchoolResponse>

    @POST("parent/login")
    fun loginParent(@Body request: RegisterSchoolRequest): Observable<ParentResponse>

    @POST("parent/resendOtp")
    fun resendOtpParents(
        @Body request: RegisterSchoolRequest,
        @Header("Authorization") token: String
    ): Observable<ParentResponse>

    @POST("school/announcement/create")
    fun createSchoolAnnouncement(
        @Body request: CreateAnnounceRequest,
        @Header("Authorization") token: String
    ): Observable<ResponseSchoolAnnouncement>

    @POST("school/notification/create")
    fun createPaymentNotification(
        @Body request: PaymentNotificationRequest,
        @Header("Authorization") token: String
    ): Observable<ResponsePaymentNotifcation>

    @GET("school/notifications")
    fun getPaymentNotification(
        @Query("schoolId") schoolId: Int,
        @Header("Authorization") token: String
    ): Observable<ResponseSchoolNotification>


    @GET("school/announcements")
    fun getSchoolAnnouncement(
        @Query("schoolId") schoolId: Int,
        @Header("Authorization") token: String
    ): Observable<ResponseAnnouncementListing>

    @GET("parent/announcement/list")
    fun getParentAnnouncement(
        @Query("parentId") schoolId: Int,
        @Header("Authorization") token: String
    ): Observable<ParentAnnouncementListing>
    @GET("parent/activity/list")
    fun getStudentActivityAnnouncement(
        @Query("parentId") schoolId: Int,
        @Header("Authorization") token: String
    ): Observable<StudentActivityResponse>

    @GET("school/emergency/notification/list")
    fun getEmergencyNotification(
        @Query("schoolId") schoolId: Int,
        @Header("Authorization") token: String
    ): Observable<ResponseEmergencyListing>

    @GET("school")
    fun getAlSchoolDetail(@Header("Authorization") token: String): Observable<RegisteredSchoolListResponse>

    @GET("school/payment/options")
    fun getAllPaymentMethod(@Header("Authorization") token: String): Observable<PaymentMethodResponse>

    @GET("school/branch/session/search")
    fun getAllBranches(
        @Query("schoolId") schoolId: Int,
        @Query("sortBy") sortBy: String = "branch",
        @Header("Authorization") token: String
    ): Observable<BranchesResponse>

    @GET("school/branch/session/search")
    fun getAllSessions(
        @Query("schoolId") schoolId: Int,
        @Query("sortBy") sortBy: String = "session",
        @Header("Authorization") token: String
    ): Observable<SessionsResponse>

    @HTTP(method = "POST", path = "student/schoolId/{schoolId}", hasBody = true)
    fun getStudentsBySchool(
        @Path("schoolId") schoolId: Int,
        @Body parentRequest: ParentIdRequest,
        @Header("Authorization") token: String
    ): Observable<StudentListResponse>

    @PATCH("school/{id}")
    fun updateSchool(
        @Path("id") schoolId: Int,
        @Body request: UpdateSchoolRequest,
        @Header("Authorization") token: String,
    ): Observable<RegisteredSchoolResponse>


    @Multipart
    @POST("school/upload/logo")
    fun uploadLogo(
        @Part uploadLogo: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Observable<UploadLogoResponse>


    @GET("student/profile/record")
    fun getStudentProfileRecord(
        @Query("schoolId") schoolId: Int,
        @Query("parentId") parentId: Int,
        @Query("studentId") studentId: Int,
        @Header("Authorization") token: String
    ): Observable<VerifyStudentFeesResponse>

    @GET("student/payment/search/")
    fun searchPayment(
        @Query("schoolId") schoolId: Int,
        @Query("sortBy") sortBy: String,
        @Header("Authorization") token: String
    ): Observable<ResponsePaidUnpaid> // Replace with your actual model


    @PUT("student/profile/school/{id}")
    fun savePaymentDetail(
        @Path("id") schoolId: Int,
        @Body request: PaymentParentRequest,
        @Header("Authorization") token: String,
    ): Observable<PaymentResponsess>

 @POST("parent/partial/payment/")
    fun savePartialPaymentDetail(
        @Body request: PartialPaymentRequest,
        @Header("Authorization") token: String,
    ): Observable<PaymentResponsess>

    @GET("student/payment/parentId/{id}")
    fun getParentPaymentDetailListing(
        @Path("id") schoolId: Int,
        @Header("Authorization") token: String,
    ): Observable<PaymentListingResponse>

    @GET("student/school/{id}")
    fun getSchoolDetails(
        @Path("id") schoolId: Int,
        @Query("parentId") parentId: Int,
        @Header("Authorization") token: String
    ): Observable<SchoolDetailsResponse>

    @GET("school/payment/received/record")
    fun getReceivedStats(
        @Query("schoolId") schoolId: Int,
        @Header("Authorization") token: String
    ): Observable<ResponseReceivedStats>


    @GET("school/payment/Due/record")
    fun getDueStats(
        @Query("schoolId") schoolId: Int,
        @Header("Authorization") token: String
    ): Observable<ResponseDueStatus>

}