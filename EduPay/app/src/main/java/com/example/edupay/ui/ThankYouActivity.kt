package com.example.edupay.ui

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.animation.AnimationUtils
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.edupay.R
import com.example.edupay.databinding.ActivityThankYouBinding
import com.example.edupay.model.regstudents.StudentReg
import com.example.edupay.pref.MyTinyDB
import com.example.edupay.utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ThankYouActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThankYouBinding
    var myTinyDB: MyTinyDB? = null
    var totalFees = 0.0
    var selectedTerm = "-"
    var institute = "-"
    var branch = "-"
    var batch = "-"
    var regNumber = "-"
    var termFees = 0.0
    var uniformFees = 0.0
    var stationaryFees = 0.0
    var transportationFees = 0.0
    private var payStudent: StudentReg? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThankYouBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myTinyDB = MyTinyDB(this)
        payStudent = myTinyDB!!.getPayStudentData(Constants.PAY_STUDENT_DATA)
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        binding.ivLogo.startAnimation(animation)

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
        totalFees = totalFees + 1
        binding.btnDownloadReceipt.setOnClickListener {
            checkAndRequestPermissions()
        }

        binding.konfettiView?.start(
            Party(
                speed = 30f,
                maxSpeed = 50f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xFFF44336.toInt(), 0xFFFFC107.toInt(), 0xFF4CAF50.toInt(), 0xFF2196F3.toInt()),
                emitter = Emitter(duration = 3, TimeUnit.SECONDS).perSecond(100),
                position = Position.Relative(0.5, 0.0)
            )
        )
    }

    fun generateAndSaveReceipt(context: Context): Uri? {
        return try {
            val pdf = PdfDocument()
            val pageWidth = 595 // A4 width in points (72 DPI)
            val pageHeight = 842 // A4 height in points
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
            val page = pdf.startPage(pageInfo)
            val canvas = page.canvas

            // Paints for different text styles
            val headerPaint = Paint().apply {
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textSize = 24f
                color = Color.BLACK
                textAlign = Paint.Align.CENTER
            }
            val subHeaderPaint = Paint().apply {
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                textSize = 12f
                color = Color.DKGRAY
                textAlign = Paint.Align.CENTER
            }
            val labelPaint = Paint().apply {
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textSize = 14f
                color = Color.BLACK
            }
            val valuePaint = Paint().apply {
                textSize = 14f
                color = Color.BLACK
            }
            val tableHeaderPaint = Paint().apply {
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textSize = 14f
                color = Color.WHITE
            }
            val footerPaint = Paint().apply {
                textSize = 10f
                color = Color.DKGRAY
                textAlign = Paint.Align.CENTER
            }
            val linePaint = Paint().apply {
                color = Color.LTGRAY
                strokeWidth = 1f
            }
            val borderPaint = Paint().apply {
                color = Color.BLACK
                strokeWidth = 2f
                style = Paint.Style.STROKE
            }

            val margin = 40f
            val labelMaxWidth = 180f // Adjusted width to accommodate longer labels
            val valueX = pageWidth - margin - 80f // Fixed value column position
            var y = margin

            // Header: Logo and Institution Details
            val logoBitmap = getBitmapFromVectorDrawable(context,
                R.drawable.default_monochrome_black, 80, 80)
            canvas.drawBitmap(logoBitmap, margin, y, null)
            y += logoBitmap.height + 10f

            canvas.drawText("Edupay Fee Receipt", pageWidth / 2f, y, headerPaint)
            y += 20f
            canvas.drawText("$institute - $branch", pageWidth / 2f, y, subHeaderPaint)
            y += 15f
            canvas.drawText("Receipt No: EDU${System.currentTimeMillis() % 100000}", pageWidth / 2f, y, subHeaderPaint)
            y += 15f
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            canvas.drawText("Date: $date", pageWidth / 2f, y, subHeaderPaint)
            y += 30f

            // Student Details
            canvas.drawRoundRect(margin, y - 10f, pageWidth - margin, y + 100f, 8f, 8f, borderPaint)
            val studentData = listOf(
                "Student Name" to (payStudent?.studentName ?: "-"),
                "Registration No" to regNumber,
                "Batch" to batch,
                "Term" to selectedTerm
            )
            for ((label, value) in studentData) {
                canvas.drawText(label, margin + 10f, y, labelPaint)
                canvas.drawText(value, margin + labelMaxWidth, y, valuePaint.apply { textAlign = Paint.Align.LEFT })
                y += 20f
            }
            y += 20f

            // Fee Breakdown Table
            canvas.drawRoundRect(margin, y - 10f, pageWidth - margin, y + 150f, 8f, 8f, borderPaint)
            val tableHeaderBg = Paint().apply { color = Color.DKGRAY }
            canvas.drawRect(margin, y - 20f, pageWidth - margin, y, tableHeaderBg)
            canvas.drawText("Description", margin + 10f, y - 5f, tableHeaderPaint)
            canvas.drawText("Amount (USD)", valueX, y - 5f, tableHeaderPaint)
            y += 5f
            canvas.drawLine(margin, y, pageWidth - margin, y, linePaint)
            y += 10f

            val feeData = listOf(
                "Term Tuition Fee" to termFees,
                "Uniform Fee" to uniformFees,
                "Stationery Fee" to stationaryFees,
                "Transportation Fee" to transportationFees,
                "Service Charge" to 1.0
            ).filter { it.second > 0.0 } // Only show non-zero fees
            for ((desc, amount) in feeData) {
                val truncatedDesc = if (desc.length > 15) "${desc.substring(0, 12)}..." else desc // Truncate to 15 chars
                canvas.drawText(truncatedDesc, margin + 10f, y, valuePaint.apply { textAlign = Paint.Align.LEFT })
                canvas.drawText(String.format("%.2f", amount), valueX, y, valuePaint.apply { textAlign = Paint.Align.RIGHT })
                y += 20f
            }
            y += 5f
            canvas.drawLine(margin, y, pageWidth - margin, y, linePaint)
            y += 15f
            canvas.drawText("Total Paid", margin + 10f, y, labelPaint)
            canvas.drawText(String.format("%.2f", totalFees), valueX, y, labelPaint.apply { textAlign = Paint.Align.RIGHT })
            y += 30f

            // Thank You Message
            canvas.drawText("Thank you for your payment!", pageWidth / 2f, y, labelPaint.apply { textAlign = Paint.Align.CENTER })
            y += 20f
            canvas.drawText("For queries, contact: support@edupay.com", pageWidth / 2f, y, subHeaderPaint)

            // Footer
            canvas.drawText(
                "This is a computer-generated receipt and does not require a signature.",
                pageWidth / 2f,
                pageHeight - margin,
                footerPaint
            )

            pdf.finishPage(page)

            // Save to Downloads
            val fileName = "Edupay_Receipt_${System.currentTimeMillis()}.pdf"
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
    }

    private val REQUEST_MEDIA_PERMISSION = 101

    private fun checkAndRequestPermissions() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                val permissionsToRequest = mutableListOf<String>()
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add(  android.Manifest.permission.READ_MEDIA_IMAGES)
                }
                if (permissionsToRequest.isNotEmpty()) {
                    ActivityCompat.requestPermissions(
                        this,
                        permissionsToRequest.toTypedArray(),
                        REQUEST_MEDIA_PERMISSION
                    )
                } else {
                    saveReceipt()
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                saveReceipt()
            }
            else -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(  android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_MEDIA_PERMISSION
                    )
                } else {
                    saveReceipt()
                }
            }
        }
    }

    private fun saveReceipt() {
        val receiptUri = generateAndSaveReceipt(this)
        receiptUri?.let {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(it, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            try {
                startActivity(intent)
                MaterialAlertDialogBuilder(this)
                    .setTitle("Download Complete")
                    .setMessage("Your receipt has been saved in the Downloads folder.")
                    .setPositiveButton("OK", null)
                    .show()
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "No PDF viewer installed", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "Failed to save receipt", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_MEDIA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveReceipt()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getBitmapFromVectorDrawable(
        context: Context,
        drawableId: Int,
        width: Int,
        height: Int
    ): Bitmap {
        val drawable = AppCompatResources.getDrawable(context, drawableId)
            ?: throw IllegalArgumentException("Drawable not found")
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun shareReceipt(uri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Share Receipt"))
    }
}