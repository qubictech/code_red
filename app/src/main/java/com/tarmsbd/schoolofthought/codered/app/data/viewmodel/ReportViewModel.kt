package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tarmsbd.schoolofthought.codered.app.data.models.Report
import com.tarmsbd.schoolofthought.codered.app.data.repository.FirebaseRepo

class ReportViewModel : ViewModel() {

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
}