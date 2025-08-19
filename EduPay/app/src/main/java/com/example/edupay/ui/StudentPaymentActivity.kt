package com.example.edupay.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edupay.databinding.ActivityStudentPaymentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.app.conatctsync.app.EduPay
import com.app.conatctsync.ui.home.HomeViewModel
import com.example.edupay.R
import com.example.edupay.adapter.SearchableAdapter
import com.example.edupay.model.parent_register.Parent
import com.example.edupay.model.payment.PaymentParentRequest
import com.example.edupay.model.regstudents.StudentReg
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.pref.PreferenceHelper
import com.example.edupay.utils.Constants
import com.example.edupay.utils.Dialogs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentPaymentActivity : BaseActivity() {
    private lateinit var binding: ActivityStudentPaymentBinding

    // Sample data
    var myTinyDB: MyTinyDB? = null
    var preferenceHelper: PreferenceHelper? = null
    private var SCHOOL_ID: Int = 0
    private val viewModel: HomeViewModel by viewModels()
    private var parentData: Parent? = null
    private var paymentDataRequest: PaymentParentRequest? = null
    private var payStudent: StudentReg? = null

    private var institute: String = ""
    private var isPartial: Boolean = false
    private var branch: String = ""
    private var regNumber: String = ""
    private var selectedTerm: String = ""
    private var batch: String = ""

    private var totalFees: Double = 0.0
    private var termFees: Double = 0.0
    private var uniformFees: Double = 0.0
    private var stationaryFees: Double = 0.0
    private var transportationFees: Double = 0.0
    private var outStandingFees: Double = 0.0
    private var paid_amount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_student_payment)
        binding = ActivityStudentPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        parentData = myTinyDB!!.getParentData(Constants.PARENT_DATA)
        paymentDataRequest = myTinyDB!!.getPaymentData(Constants.PAYMENT_DATA_REQUEST)
        payStudent = myTinyDB!!.getPayStudentData(Constants.PAY_STUDENT_DATA)

        preferenceHelper = PreferenceHelper(this)
        // Get data from previous activity

        //  val instituteContainer = findViewById<LinearLayout>(R.id.instituteContainer)
        // val branchContainer = findViewById<LinearLayout>(R.id.branchContainer)

        SCHOOL_ID = intent.getIntExtra("SCHOOL_ID", 0)
        institute = intent.getStringExtra("INSTITUTE") ?: ""
        branch = intent.getStringExtra("BRANCH") ?: ""
        regNumber = intent.getStringExtra("REG_NUMBER") ?: ""
        totalFees = intent.getDoubleExtra("TOTAL_FEES", 0.0)
        selectedTerm = intent.getStringExtra("SELECTED_TERM") ?: ""
        batch = intent.getStringExtra("BATCH") ?: ""
        termFees = intent.getDoubleExtra("TERM_FEES", 0.0)
        uniformFees = intent.getDoubleExtra("UNIFORM_FEES", 0.0)
        stationaryFees = intent.getDoubleExtra("STATIONARY_FEES", 0.0)
        transportationFees = intent.getDoubleExtra("TRANSPORTATION_FEES", 0.0)
        outStandingFees = intent.getDoubleExtra("OUTSTANDING_FEES", 0.0)
        paid_amount = intent.getDoubleExtra("PAID_AMOUNT", 0.0)

        // Set selected details
        binding.tvStudentName.text = "${payStudent?.studentName}"
        binding.tvStudentGrade.text = "${payStudent?.className}"
        binding.tvSelectedInstitute.text = "$institute"
        binding.tvSelectedBranch.text = "$branch"
        binding.tvSelectedRegNumber.text = "$regNumber"
        val totalFeesWithCharge = totalFees + 1
        binding.tvTotalAmount.text = "$$totalFeesWithCharge"
        //  binding.tvSelectedTerm.text = selectedTerm

        binding.tvStudentBatch.text = batch
        // binding.tvRegNumber.text = regNumber
        binding.tvTermFee.text = "$$termFees"
        binding.tvUniformFee.text = "$$uniformFees"
        binding.tvStationaryFee.text = "$$stationaryFees"
        binding.tvTransportFee.text = "$$transportationFees"
        binding.tvOutstandingFee.text = "$$outStandingFees"
        binding.tvPaidAmountFee.text = "- $$paid_amount"

        if(outStandingFees>0)
        {
            binding.lnOutstanding.visibility=View.VISIBLE
            binding.lnPaidAmount.visibility=View.VISIBLE
            val totalFees = (totalFees-paid_amount) + 1
            isPartial=true
            binding.tvTotalAmount.text = "$$totalFees"

        }else
        {
            binding.lnOutstanding.visibility=View.GONE
            binding.lnPaidAmount.visibility=View.GONE
            isPartial=false
        }
        // Set up download button
        binding.btnDownloadReceipt.setOnClickListener {
            // Handle download receipt logic

            showDownloadSuccessDialog()
        }

        // Set up search button
        binding.btnPay.setOnClickListener {
            // Handle search logic
            //checkAndRequestPermissions()
            //   val file= generateAndSaveReceipt(this@StudentPaymentActivity)
        //    savePaymentDetail()

            val intent = Intent(this, SelectPaymentMethodActivity::class.java).apply {
                putExtra("TOTAL_FEES", paid_amount)
                putExtra("INSTITUTE", institute)
                putExtra("BRANCH", branch)
                putExtra("BATCH", batch)
                //   putExtra("TERM_NAME", selectedTermText)
                putExtra("TERM_FEES", totalFees)
                putExtra("REG_NUMBER", regNumber)
                putExtra("TERM_FEES", paid_amount)
                putExtra("UNIFORM_FEES",uniformFees)
                putExtra("STATIONARY_FEES", stationaryFees)
                putExtra("TRANSPORTATION_FEES", transportationFees)
                putExtra("SCHOOL_ID", SCHOOL_ID)
                putExtra("IS_FROM_PARTIAL_PAYMENT", isPartial)
                putExtra("OUTSTANDING_FEES", outStandingFees)
                putExtra("PAID_AMOUNT", paid_amount)
                //     putExtra("PARENT_ID", PARENT_ID)
           //     putExtra("STUDENT_ID", STUDENT_ID)
            }
            startActivity(intent)
finish()

        }
        // Institute selection
        /*      instituteContainer.setOnClickListener {
                  showSearchableBottomSheet(
                      title = "Select Institute",
                      items = institutes,
                      onItemSelected = { selected ->
                          tvInstitute.text = selected
                          // Reset branch when institute changes
                          tvBranch.text = ""
                      }
                  )
              }*/

        // Branch selection (only if institute is selected)
        /*  branchContainer.setOnClickListener {
              if (tvInstitute.text.isNotEmpty()) {
                  val branches = branchesMap[tvInstitute.text.toString()] ?: emptyList()
                  showSearchableBottomSheet(
                      title = "Select Branch",
                      items = branches,
                      onItemSelected = { selected ->
                          tvBranch.text = selected
                      }
                  )
              } else {
                  Toast.makeText(this, "Please select institute first", Toast.LENGTH_SHORT).show()
              }
          }*/


    }

    private fun showSearchableBottomSheet(
        title: String,
        items: List<String>,
        onItemSelected: (String) -> Unit
    ) {
        val bottomSheet: BottomSheetDialog = BottomSheetDialog(this)
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_searchable, null)

        val searchField: TextInputEditText = view.findViewById(R.id.searchField)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val adapter: SearchableAdapter =
            SearchableAdapter(items) { selectedItem: String ->
                onItemSelected(selectedItem)
                bottomSheet.dismiss()
            }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        bottomSheet.setContentView(view)
        bottomSheet.show()
    }


    fun showDownloadSuccessDialog() {
        val dialog = Dialog(this).apply {
            setContentView(R.layout.custom_success_dialog)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            // Set OK button click listener
            findViewById<MaterialButton>(R.id.btnOk).setOnClickListener {
                dismiss()
            }

            // Add enter animation
            window?.setWindowAnimations(R.style.DialogAnimation)
        }

        dialog.show()
    }
    /* fun generateAndSaveReceipt(context: Context): Uri? {
         return try {
             val pdf = PdfDocument()
             val pageWidth = 400
             val pageHeight = 700
             val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
             val page = pdf.startPage(pageInfo)
             val canvas = page.canvas

             val titlePaint = Paint().apply {
                 typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
                 textSize = 18f
                 color = Color.BLACK
             }

             val labelPaint = Paint().apply {
                 textSize = 14f
                 color = Color.BLACK
             }

             val valuePaint = Paint().apply {
                 textSize = 14f
                 color = Color.BLACK
                 textAlign = Paint.Align.RIGHT
             }

             val linePaint = Paint().apply {
                 color = Color.LTGRAY
                 strokeWidth = 1f
             }

             // Title
             var y = 50f
             val startX = 40f
             val endX = pageWidth - 40f
             canvas.drawText("Student Fee Receipt", pageWidth / 2f - 80f, y, titlePaint)
             y += 40f

             val data = mapOf(
                 "Student Name" to "John Doe",
                 "Registration No" to "B123456",
                 "Institute" to "ABC Institute of Technology",
                 "Branch" to "Computer Science",
                 "Batch" to "2022–2025",
                 "Term" to "Spring 2025",
                 "Term Tuition Fee" to "$500",
                 "Uniform Fee" to "$50",
                 "Stationary Fee" to "$30",
                 "Transportation Fee" to "$100",
                 "Service Charge" to "$1",
                 "Total Paid" to "$681"
             )

             for ((label, value) in data) {
                 if (y + 40 > pageHeight) break  // avoid writing out of bounds
                 canvas.drawText(label, startX, y, labelPaint)
                 canvas.drawText(value, endX, y, valuePaint)
                 y += 25f
                 canvas.drawLine(startX, y, endX, y, linePaint)
                 y += 10f
             }

             y += 20f
             canvas.drawText("Thank you for your payment!", pageWidth / 2f - 100f, y, labelPaint)

             pdf.finishPage(page)

             // File setup
             val fileName = "Student_Fee_Receipt_${System.currentTimeMillis()}.pdf"
             val contentValues = ContentValues().apply {
                 put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                 put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                     put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                     put(MediaStore.MediaColumns.IS_PENDING, 1)
                 }
             }

             val resolver = context.contentResolver
             val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                 ?: return null

             resolver.openOutputStream(uri)?.use { outputStream ->
                 pdf.writeTo(outputStream)
             }

             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                 contentValues.clear()
                 contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                 resolver.update(uri, contentValues, null, null)
             }

             pdf.close()
             uri
         } catch (e: Exception) {
             e.printStackTrace()
             null
         }
     }*/


}