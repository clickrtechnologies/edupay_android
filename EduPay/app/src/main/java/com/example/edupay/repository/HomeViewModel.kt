package com.app.conatctsync.ui.home

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.repository.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepostory,
    application: Application
) : ViewModel() {
    private val context = application.applicationContext
    private val preferenceHelper = PreferenceHelper(context)

    //val allContacts: LiveData<List<ContactUser>> = repository.()
    var email: String? = null
    var password: String? = null
    var fullName: String? = null
    var selectedValueName: Int? = null

    var openLoginScree = MutableLiveData<Boolean>(false)
    val openSignupScreen = MutableLiveData<Boolean>(false)

    var isHomePress = ObservableField<Boolean>()
    var questionNumber = ObservableField<Int>()
    var marketSelectedposition = ObservableField<Int>()
    var isContactsPress = ObservableField<Boolean>()
    var isNotificationPress = ObservableField<Boolean>()
    var isSettingPress = ObservableField<Boolean>()
    var zoLearnPress = ObservableField<Boolean>()
    var onBottomTabPress: SingleLiveEvent<Int> = SingleLiveEvent()

    var isBreakFastPress = ObservableField<Boolean>()
    var isLunchPress = ObservableField<Boolean>()
    var isDinnerPress = ObservableField<Boolean>()
    var isSnackPress = ObservableField<Boolean>()
    var onMealSelectionPress: SingleLiveEvent<Int> = SingleLiveEvent()

    var last7DayPress = ObservableField<Boolean>()
    var last30DayPress = ObservableField<Boolean>()
    var last60DayPress = ObservableField<Boolean>()
    var last90DayPress = ObservableField<Boolean>()
    var onDaySelectionPress: SingleLiveEvent<Int> = SingleLiveEvent()

    /*    fun openSignUpScreen(view:View){
            view.context.startActivity(Intent(view.context,SignUpActivity::class.java))
            openSignupScreen.value=true
        }*/

    /*
        fun onBottomPress(value: Int) {
            isHomePress.set(value == Constant.DASHBOARD)
            isContactsPress.set(value == Constant.CONTACTS)
            isNotificationPress.set(value == Constant.NOTIFICATION)
            isSettingPress.set(value == Constant.SETTINGS)
            //  zoLearnPress.set(value == 5)
            onBottomTabPress.setValue(value)
        }*/


    // Insert contact in background thread
    /*  fun insertContactDetail(contact: ContactUser) {
          viewModelScope.launch(Dispatchers.IO) {
              repository.insertContactDetail(contact)
          }
      }*/

    /*fun resetPositionForReviewedContacts() {
           viewModelScope.launch(Dispatchers.IO) {
               repository.resetPositionForReviewedContacts()
           }
       }
    fun deleteAllRecord() {
           viewModelScope.launch(Dispatchers.IO) {
               repository.deleteAllContacts()
           }
       }
   */
    fun registeredSchool(request: RegisterSchoolRequest): LiveData<RegisteredSchoolResponse> {
        return repository.registeredSchool(request)
    }

    fun registeredParent(request: RegisterSchoolRequest): LiveData<ParentResponse> {
        return repository.registeredParent(request)
    }

    fun updateParentInfo(parentId: Int, request: UpdateParentDetail): LiveData<ParentResponse> {
        return repository.updateParentInfo(parentId, request)
    }

    fun loginSchool(request: RegisterSchoolRequest): LiveData<RegisteredSchoolResponse> {
        return repository.loginSchool(request)
    }

 fun resendOtpSchool(request: RegisterSchoolRequest): LiveData<RegisteredSchoolResponse> {
        return repository.resendOtpSchool(request)
    }

    fun loginParent(request: RegisterSchoolRequest): LiveData<ParentResponse> {
        return repository.loginParent(request)
    }
  fun resendOtpParents(request: RegisterSchoolRequest): LiveData<ParentResponse> {
        return repository.resendOtpParents(request)
    }

    fun createSchoolAnnouncement(request: CreateAnnounceRequest): LiveData<ResponseSchoolAnnouncement> {
        return repository.createSchoolAnnouncement(request)
    }

 fun createPaymentNotification(request: PaymentNotificationRequest): LiveData<ResponsePaymentNotifcation> {
        return repository.createPaymentNotification(request)
    }

    fun getPaymentNotification(schoolid: Int): LiveData<ResponseSchoolNotification> {
        return repository.getPaymentNotification(schoolid)
    }

    fun getSchoolAnnouncement(schoolid: Int): LiveData<ResponseAnnouncementListing> {
        return repository.getSchoolAnnouncement(schoolid)
    }
    fun getParentAnnouncement(parentId: Int): LiveData<ParentAnnouncementListing> {
        return repository.getParentAnnouncement(parentId)
    }
 fun getStudentActivityAnnouncement(parentId: Int): LiveData<StudentActivityResponse> {
        return repository.getStudentActivityAnnouncement(parentId)
    }
fun getEmergencyNotification(schoolid: Int): LiveData<ResponseEmergencyListing> {
        return repository.getEmergencyNotification(schoolid)
    }

    fun updateSchool(
        schoolid: Int,
        request: UpdateSchoolRequest
    ): LiveData<RegisteredSchoolResponse> {
        return repository.updateSchool(schoolid, request)
    }

 fun savePaymentDetail(
        schoolid: Int,
        request: PaymentParentRequest
    ): LiveData<PaymentResponsess> {
        return repository.savePaymentDetail(schoolid, request)
    }
fun savePartialPaymentDetail(

        request: PartialPaymentRequest
    ): LiveData<PaymentResponsess> {
        return repository.savePartialPaymentDetail( request)
    }

fun getParentPaymentDetailListing(
        parentId: Int,
    ): LiveData<PaymentListingResponse> {
        return repository.getParentPaymentDetailListing(parentId)
    }
fun getReceivedStats(
    schoolId: Int,
    ): LiveData<ResponseReceivedStats> {
        return repository.getReceivedStats(schoolId)
    }

fun getDueStats(
    schoolId: Int,
    ): LiveData<ResponseDueStatus> {
        return repository.getDueStats(schoolId)
    }

    fun getAlSchoolDetail(
    ): LiveData<RegisteredSchoolListResponse> {
        return repository.getAlSchoolDetail()
    }

 fun getAllPaymentMethod(
    ): LiveData<PaymentMethodResponse> {
        return repository.getAllPaymentMethod()
    }

    fun getAllBranches(
        schoolId: Int
    ): LiveData<BranchesResponse> {
        return repository.getAllBranches(schoolId)
    }
 fun getSchoolDetails(
        schoolId: Int,
        parentId: Int,
    ): LiveData<SchoolDetailsResponse> {
        return repository.getSchoolDetails(schoolId,parentId)
    }

    fun getAllSessions(
        schoolId: Int
    ): LiveData<SessionsResponse> {
        return repository.getAllSessions(schoolId)
    }

    fun getStudentProfileRecord(
        schoolId: Int,
        parentId: Int,
        studentId: Int
    ): LiveData<VerifyStudentFeesResponse> {
        return repository.getStudentProfileRecord(schoolId, parentId, studentId)
    }
  fun searchPayment(
        schoolId: Int,
        termType: String
    ): LiveData<ResponsePaidUnpaid> {
        return repository.searchPayment(schoolId, termType)
    }

    fun getStudentsBySchool(
        schoolId: Int, request: ParentIdRequest
    ): LiveData<StudentListResponse> {
        return repository.getStudentsBySchool(schoolId, request)
    }


    fun updateSchoolLogo(schoolImagefile: File): LiveData<UploadLogoResponse> {
        return repository.updateSchoolLogo(schoolImagefile)
    }


}