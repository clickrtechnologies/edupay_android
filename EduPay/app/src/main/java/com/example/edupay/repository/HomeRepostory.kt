package com.app.conatctsync.ui.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.conatctsync.network.SafeApiCall
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
import com.example.edupay.network.NetworkUtils
import com.example.edupay.network.RxUtils
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
class HomeRepostory @Inject constructor(@ApplicationContext private val mContext: Context) : SafeApiCall {
    private val preferenceHelper: PreferenceHelper = PreferenceHelper(mContext)
  @SuppressLint("CheckResult", "SuspiciousIndentation")
  fun registeredSchool(request: RegisterSchoolRequest): LiveData<RegisteredSchoolResponse> {
     val data = MutableLiveData<RegisteredSchoolResponse>()
      //  val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
      request.deviceToken=preferenceHelper.getValue(Constants.FIREBASE_TOKEN,"")
        NetworkUtils.getAPIService()
            .registeredSchool(request)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: RegisteredSchoolResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = RegisteredSchoolResponse("fail", "",null)
                data.value = errorResponse

           })

        return data
    }
 @SuppressLint("CheckResult", "SuspiciousIndentation")
  fun registeredParent(request: RegisterSchoolRequest): LiveData<ParentResponse> {
     val data = MutableLiveData<ParentResponse>()
  //   val authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWphQGdtYWlsLmNvbSIsImlhdCI6MTc0ODU4MDU2MiwiZXhwIjoxNzc5Njg0NTYyfQ.KOKB-VfmZ_2LBKKKqw5z1q8gKP9z7f8kkJHfNQghvI8"
     request.deviceToken=preferenceHelper.getValue(Constants.FIREBASE_TOKEN,"")

        NetworkUtils.getAPIService()
            .registeredParent(request)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ParentResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ParentResponse("fail", "",null,null,null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun updateParentInfo(schoolid:Int,request: UpdateParentDetail): LiveData<ParentResponse> {
     val data = MutableLiveData<ParentResponse>()
  //   val authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWphQGdtYWlsLmNvbSIsImlhdCI6MTc0ODU4MDU2MiwiZXhwIjoxNzc5Njg0NTYyfQ.KOKB-VfmZ_2LBKKKqw5z1q8gKP9z7f8kkJHfNQghvI8"
    val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"

        NetworkUtils.getAPIService()
            .updateParentInfo(schoolid,request,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ParentResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ParentResponse("fail", "",null,null,null)
                data.value = errorResponse

           })

        return data
    }

 @SuppressLint("CheckResult", "SuspiciousIndentation")
  fun loginSchool(request: RegisterSchoolRequest): LiveData<RegisteredSchoolResponse> {
     val data = MutableLiveData<RegisteredSchoolResponse>()
     request.deviceToken=preferenceHelper.getValue(Constants.FIREBASE_TOKEN,"")

     //  val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
        NetworkUtils.getAPIService()
            .loginSchool(request)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: RegisteredSchoolResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = RegisteredSchoolResponse("fail", "",null)
                data.value = errorResponse

           })

        return data
    }
 @SuppressLint("CheckResult", "SuspiciousIndentation")
  fun resendOtpSchool(request: RegisterSchoolRequest): LiveData<RegisteredSchoolResponse> {
     val data = MutableLiveData<RegisteredSchoolResponse>()
     request.deviceToken=preferenceHelper.getValue(Constants.FIREBASE_TOKEN,"")
     val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"

     //  val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
        NetworkUtils.getAPIService()
            .resendOtpSchool(request,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: RegisteredSchoolResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = RegisteredSchoolResponse("fail", "",null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun loginParent(request: RegisterSchoolRequest): LiveData<ParentResponse> {
     val data = MutableLiveData<ParentResponse>()
    request.deviceToken=preferenceHelper.getValue(Constants.FIREBASE_TOKEN,"")

    //  val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
        NetworkUtils.getAPIService()
            .loginParent(request)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ParentResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ParentResponse("fail", "",null,null,null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun resendOtpParents(request: RegisterSchoolRequest): LiveData<ParentResponse> {
     val data = MutableLiveData<ParentResponse>()
   request.deviceToken=preferenceHelper.getValue(Constants.FIREBASE_TOKEN,"")
    val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"

    //  val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
        NetworkUtils.getAPIService()
            .resendOtpParents(request,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ParentResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ParentResponse("fail", "",null,null,null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun createSchoolAnnouncement(request: CreateAnnounceRequest): LiveData<ResponseSchoolAnnouncement> {
     val data = MutableLiveData<ResponseSchoolAnnouncement>()
   // val authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWphQGdtYWlsLmNvbSIsImlhdCI6MTc0ODU4MDU2MiwiZXhwIjoxNzc5Njg0NTYyfQ.KOKB-VfmZ_2LBKKKqw5z1q8gKP9z7f8kkJHfNQghvI8"
    val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"

   // val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
        NetworkUtils.getAPIService()
            .createSchoolAnnouncement(request,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ResponseSchoolAnnouncement ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ResponseSchoolAnnouncement("fail",null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun createPaymentNotification(request: PaymentNotificationRequest): LiveData<ResponsePaymentNotifcation> {
     val data = MutableLiveData<ResponsePaymentNotifcation>()
   // val authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWphQGdtYWlsLmNvbSIsImlhdCI6MTc0ODU4MDU2MiwiZXhwIjoxNzc5Njg0NTYyfQ.KOKB-VfmZ_2LBKKKqw5z1q8gKP9z7f8kkJHfNQghvI8"
    val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"

   // val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
        NetworkUtils.getAPIService()
            .createPaymentNotification(request,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ResponsePaymentNotifcation ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ResponsePaymentNotifcation("fail",null)
                data.value = errorResponse

           })

        return data
    }

@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getSchoolAnnouncement(schoolId: Int): LiveData<ResponseAnnouncementListing> {
     val data = MutableLiveData<ResponseAnnouncementListing>()
    val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"

    // val authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWphQGdtYWlsLmNvbSIsImlhdCI6MTc0ODU4MDU2MiwiZXhwIjoxNzc5Njg0NTYyfQ.KOKB-VfmZ_2LBKKKqw5z1q8gKP9z7f8kkJHfNQghvI8"
     //  val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
        NetworkUtils.getAPIService()
            .getSchoolAnnouncement(schoolId,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ResponseAnnouncementListing ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ResponseAnnouncementListing("fail",null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getParentAnnouncement(schoolId: Int): LiveData<ParentAnnouncementListing> {
     val data = MutableLiveData<ParentAnnouncementListing>()
    val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"

    // val authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWphQGdtYWlsLmNvbSIsImlhdCI6MTc0ODU4MDU2MiwiZXhwIjoxNzc5Njg0NTYyfQ.KOKB-VfmZ_2LBKKKqw5z1q8gKP9z7f8kkJHfNQghvI8"
     //  val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
        NetworkUtils.getAPIService()
            .getParentAnnouncement(schoolId,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ParentAnnouncementListing ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ParentAnnouncementListing("fail",null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getStudentActivityAnnouncement(parentId: Int): LiveData<StudentActivityResponse> {
     val data = MutableLiveData<StudentActivityResponse>()
    val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"

    // val authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWphQGdtYWlsLmNvbSIsImlhdCI6MTc0ODU4MDU2MiwiZXhwIjoxNzc5Njg0NTYyfQ.KOKB-VfmZ_2LBKKKqw5z1q8gKP9z7f8kkJHfNQghvI8"
     //  val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
        NetworkUtils.getAPIService()
            .getStudentActivityAnnouncement(parentId,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: StudentActivityResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = StudentActivityResponse(null,"fail")
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getEmergencyNotification(schoolId: Int): LiveData<ResponseEmergencyListing> {
     val data = MutableLiveData<ResponseEmergencyListing>()
    val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"

    // val authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWphQGdtYWlsLmNvbSIsImlhdCI6MTc0ODU4MDU2MiwiZXhwIjoxNzc5Njg0NTYyfQ.KOKB-VfmZ_2LBKKKqw5z1q8gKP9z7f8kkJHfNQghvI8"
     //  val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
        NetworkUtils.getAPIService()
            .getEmergencyNotification(schoolId,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ResponseEmergencyListing ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ResponseEmergencyListing("fail",null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getPaymentNotification(schoolId: Int): LiveData<ResponseSchoolNotification> {
     val data = MutableLiveData<ResponseSchoolNotification>()
    val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"

    // val authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWphQGdtYWlsLmNvbSIsImlhdCI6MTc0ODU4MDU2MiwiZXhwIjoxNzc5Njg0NTYyfQ.KOKB-VfmZ_2LBKKKqw5z1q8gKP9z7f8kkJHfNQghvI8"
     //  val authorizationHeader = "Bearer ${Constants.AUTHORIZATION_HEADER}"
        NetworkUtils.getAPIService()
            .getPaymentNotification(schoolId,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ResponseSchoolNotification ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ResponseSchoolNotification("fail",null)
                data.value = errorResponse

           })

        return data
    }

 @SuppressLint("CheckResult", "SuspiciousIndentation")
  fun updateSchool(schoolid:Int,request: UpdateSchoolRequest): LiveData<RegisteredSchoolResponse> {
     val data = MutableLiveData<RegisteredSchoolResponse>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .updateSchool(schoolid,request,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: RegisteredSchoolResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = RegisteredSchoolResponse("fail", "",null)
                data.value = errorResponse

           })

        return data
    }
 @SuppressLint("CheckResult", "SuspiciousIndentation")
  fun savePaymentDetail(schoolid:Int,request: PaymentParentRequest): LiveData<PaymentResponsess> {
     val data = MutableLiveData<PaymentResponsess>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .savePaymentDetail(schoolid,request,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: PaymentResponsess ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = PaymentResponsess("fail",null)
                data.value = errorResponse

           })

        return data
    }

    @SuppressLint("CheckResult", "SuspiciousIndentation")
  fun savePartialPaymentDetail(request: PartialPaymentRequest): LiveData<PaymentResponsess> {
     val data = MutableLiveData<PaymentResponsess>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .savePartialPaymentDetail(request,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: PaymentResponsess ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = PaymentResponsess("fail",null)
                data.value = errorResponse

           })

        return data
    }
 @SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getParentPaymentDetailListing(parentId:Int): LiveData<PaymentListingResponse> {
     val data = MutableLiveData<PaymentListingResponse>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .getParentPaymentDetailListing(parentId,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: PaymentListingResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = PaymentListingResponse("fail",null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getAlSchoolDetail(): LiveData<RegisteredSchoolListResponse> {
     val data = MutableLiveData<RegisteredSchoolListResponse>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .getAlSchoolDetail(authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: RegisteredSchoolListResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = RegisteredSchoolListResponse("fail", "",null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getSchoolDetails(schoolId:Int,parentId:Int): LiveData<SchoolDetailsResponse> {
     val data = MutableLiveData<SchoolDetailsResponse>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .getSchoolDetails(schoolId,parentId,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: SchoolDetailsResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = SchoolDetailsResponse("fail", null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getAllPaymentMethod(): LiveData<PaymentMethodResponse> {
     val data = MutableLiveData<PaymentMethodResponse>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .getAllPaymentMethod(authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: PaymentMethodResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = PaymentMethodResponse("fail",null)
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getAllBranches(schoolId:Int): LiveData<BranchesResponse> {
     val data = MutableLiveData<BranchesResponse>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .getAllBranches(schoolId,"branch",authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: BranchesResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = BranchesResponse(null,"fail")
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getAllSessions(schoolId:Int): LiveData<SessionsResponse> {
     val data = MutableLiveData<SessionsResponse>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .getAllSessions(schoolId,"session",authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: SessionsResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = SessionsResponse(null,"fail")
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getStudentProfileRecord(schoolId:Int,parentId:Int,studentId:Int): LiveData<VerifyStudentFeesResponse> {
     val data = MutableLiveData<VerifyStudentFeesResponse>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .getStudentProfileRecord(schoolId,parentId,studentId,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: VerifyStudentFeesResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = VerifyStudentFeesResponse(null,"fail")
                data.value = errorResponse

           })

        return data
    }
@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun searchPayment(schoolId:Int,termType:String): LiveData<ResponsePaidUnpaid> {
     val data = MutableLiveData<ResponsePaidUnpaid>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .searchPayment(schoolId,termType,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ResponsePaidUnpaid ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ResponsePaidUnpaid("fail",null)
                data.value = errorResponse

           })

        return data
    }

@SuppressLint("CheckResult", "SuspiciousIndentation")
  fun getStudentsBySchool(schoolId:Int,request: ParentIdRequest): LiveData<StudentListResponse> {
     val data = MutableLiveData<StudentListResponse>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .getStudentsBySchool(schoolId,request,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: StudentListResponse ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = StudentListResponse("fail",null,)
                data.value = errorResponse

           })

        return data
    }

    @SuppressLint("CheckResult")
    fun updateSchoolLogo(filePath: File): LiveData<UploadLogoResponse> {
        val requestFile = filePath.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("uploadLogo", filePath.name, requestFile)

        val data = MutableLiveData<UploadLogoResponse>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN, "")}"

        NetworkUtils.getAPIService()
            .uploadLogo(multipartBody, authorizationHeader) // Make sure this matches your API definition
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: UploadLogoResponse ->
                data.value = response
            }, { error: Throwable ->
                data.value = UploadLogoResponse(message = error.message ?: "Unknown error", url = "")
            })

        return data
    }

    @SuppressLint("CheckResult", "SuspiciousIndentation")
    fun getReceivedStats(schoolId:Int): LiveData<ResponseReceivedStats> {
        val data = MutableLiveData<ResponseReceivedStats>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .getReceivedStats(schoolId,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ResponseReceivedStats ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ResponseReceivedStats(null,"fail","")
                data.value = errorResponse

            })

        return data
    }

    @SuppressLint("CheckResult", "SuspiciousIndentation")
    fun getDueStats(schoolId:Int): LiveData<ResponseDueStatus> {
        val data = MutableLiveData<ResponseDueStatus>()
        val authorizationHeader = "Bearer ${preferenceHelper.getValue(Constants.ACCESS_TOKEN,"")}"
        NetworkUtils.getAPIService()
            .getDueStats(schoolId,authorizationHeader)
            .compose(RxUtils.applySchedulers())
            .subscribe({ response: ResponseDueStatus ->
                data.value = response
            }, { e: Throwable ->
                val errorResponse = ResponseDueStatus(null,"fail","")
                data.value = errorResponse

            })

        return data
    }

}