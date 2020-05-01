package com.tarmsbd.schoolofthought.codered.app.ui.report

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.ReportViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.ActivityReportBinding
import java.util.logging.Logger

class ReportActivity : AppCompatActivity() {
    private lateinit var activityReportBinding: ActivityReportBinding
    private lateinit var reportViewModel: ReportViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reportViewModel = ViewModelProvider(this)[ReportViewModel::class.java]

        activityReportBinding = DataBindingUtil.setContentView(this, R.layout.activity_report)
        activityReportBinding.apply {
            viewModel = reportViewModel
            lifecycleOwner = this@ReportActivity
        }

        reportViewModel.getReportData.observe(this, Observer { report ->
            if (report.patientName.isNotEmpty() && report.mobile.isNotEmpty() && report.location.isNotEmpty() && report.desc.isNotEmpty()) {
                activityReportBinding.submitReport.isEnabled = true
                activityReportBinding.submitReport.setOnClickListener {
                    reportViewModel.submitReportData(report)
                    showDialog()
                    // todo: latlng for user location
                }
            } else {
                activityReportBinding.submitReport.isEnabled = false
                activityReportBinding.submitReport.setOnClickListener(null)
            }

            Logger.getLogger("Report").warning(report.toString())
        })
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(this)
            .setMessage("Report Sent!")
            .setCancelable(false)
            .setPositiveButton("Okay!") { dialogInterface, i ->
                dialogInterface.dismiss()
                finish()
            }.create()

        dialog.show()
    }
}
