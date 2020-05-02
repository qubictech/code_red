package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import android.app.DatePickerDialog
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tarmsbd.schoolofthought.codered.app.data.models.Report
import com.tarmsbd.schoolofthought.codered.app.data.repository.FirebaseRepo
import java.util.*

class ReportViewModel : ViewModel() {

    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)


    private var mReportData = MutableLiveData<Report>()
    private var report: Report = Report()

    var name = ""
        set(value) {
            field = value
            report.patientName = value
            mReportData.value = report
        }

    var mobile = ""
        set(value) {
            field = value
            report.mobile = value
            mReportData.value = report
        }

    var location = ""
        set(value) {
            field = value
            report.location = value
            mReportData.value = report
        }

    var desc = ""
        set(value) {
            field = value
            report.desc = value
            mReportData.value = report
        }

    val getReportData: LiveData<Report> = mReportData

    fun submitReportData(report: Report) {
        FirebaseRepo.submitReportData(report)
        this.report = Report()

        mReportData.value = report
    }
    fun showDatePicker(view: View) {
        DatePickerDialog(
            view.context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // do stuff
                Log.d(TAG, "DatePickerDialog: $year/${monthOfYear + 1}/$dayOfMonth")

            },
            year,
            month,
            day
        ).show()
    }
}