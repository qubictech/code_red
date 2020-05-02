package com.tarmsbd.schoolofthought.codered.app.ui.report

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.ReportViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.ActivityReportBinding
import com.tarmsbd.schoolofthought.codered.app.ui.main.GoogleMapActivity
import com.tarmsbd.schoolofthought.codered.app.utils.MyPatterns
import java.util.logging.Logger

class ReportActivity : AppCompatActivity() {
    private lateinit var activityReportBinding: ActivityReportBinding
    private lateinit var reportViewModel: ReportViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var mLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        reportViewModel = ViewModelProvider(this)[ReportViewModel::class.java]

        activityReportBinding = DataBindingUtil.setContentView(this, R.layout.activity_report)
        activityReportBinding.apply {
            viewModel = reportViewModel
            lifecycleOwner = this@ReportActivity
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        reportViewModel.getReportData.observe(this, Observer { report ->
            if (report.patientName.isNotEmpty() && report.mobile.isNotEmpty() && MyPatterns.NUMBER_PATTERN.matches(
                    report.mobile
                ) && mLocation != null && report.desc.isNotEmpty()
            ) {

                activityReportBinding.submitReport.isEnabled = true
                activityReportBinding.submitReport.setOnClickListener {
                    if (report.desc.length >= 100) {
                        reportViewModel.submitReportData(report)
                        showDialog()
                    } else {
                        Toast.makeText(
                            this,
                            "Report must be at least 100+ chars",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

            } else {
                activityReportBinding.submitReport.isEnabled = false
                activityReportBinding.submitReport.setOnClickListener(null)
            }

            Logger.getLogger("Report").warning(report.toString())
        })

        // initially set male clicked!
        activityReportBinding.genderMale.background.setColorFilter(
            Color.parseColor("#EE2B60"),
            PorterDuff.Mode.SRC_ATOP
        )

        activityReportBinding.genderMale.setOnClickListener {
            reportViewModel.setGender("M")
            activityReportBinding.genderMale.background.setColorFilter(
                Color.parseColor("#EE2B60"),
                PorterDuff.Mode.SRC_ATOP
            )

            activityReportBinding.genderFemale.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )

            activityReportBinding.genderOthers.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )

        }

        activityReportBinding.genderFemale.setOnClickListener {
            reportViewModel.setGender("F")

            activityReportBinding.genderMale.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )

            activityReportBinding.genderFemale.background.setColorFilter(
                Color.parseColor("#EE2B60"),
                PorterDuff.Mode.SRC_ATOP
            )

            activityReportBinding.genderOthers.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        activityReportBinding.genderOthers.setOnClickListener {
            reportViewModel.setGender("O")

            activityReportBinding.genderMale.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )

            activityReportBinding.genderFemale.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )

            activityReportBinding.genderOthers.background.setColorFilter(
                Color.parseColor("#EE2B60"),
                PorterDuff.Mode.SRC_ATOP
            )
        }
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

    override fun onStart() {
        super.onStart()

        if (checkPermissions()) {
            if (isLocationEnabled()) {

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        mLocation = if (location == null) {
                            LatLng(23.7536267, 90.376229)
                        } else {
                            LatLng(location.latitude, location.longitude)
                        }
                    }
            } else {
                //location is not enabled!
                buildAlertMessageNoGps()
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            GoogleMapActivity.PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == GoogleMapActivity.PERMISSION_ID) {
            if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission not granted
                requestPermissions()
            }
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: android.app.AlertDialog = builder.create()
        alert.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
