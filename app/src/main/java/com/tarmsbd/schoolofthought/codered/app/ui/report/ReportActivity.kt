package com.tarmsbd.schoolofthought.codered.app.ui.report

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.databinding.ActivityReportBinding

class ReportActivity : AppCompatActivity() {
    private lateinit var activityReportBinding: ActivityReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityReportBinding = DataBindingUtil.setContentView(this, R.layout.activity_report)
    }
}
