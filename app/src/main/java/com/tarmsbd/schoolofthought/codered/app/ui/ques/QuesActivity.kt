package com.tarmsbd.schoolofthought.codered.app.ui.ques

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.data.models.SelfResult
import com.tarmsbd.schoolofthought.codered.app.data.repository.FirebaseRepo
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.MainViewModel
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.QuesViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.ActivityQuesBinding
import com.tarmsbd.schoolofthought.codered.app.ui.main.GoogleMapActivity
import com.tarmsbd.schoolofthought.codered.app.ui.sos.SOSActivity
import java.util.logging.Logger

class QuesActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var quesViewModel: QuesViewModel
    private var id: Int = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val user = FirebaseAuth.getInstance().currentUser!!

    private var mLocation: LatLng = LatLng(0.0, 0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quesViewModel = ViewModelProvider(this)[QuesViewModel::class.java]

        val activityQuesBinding: ActivityQuesBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_ques)

        activityQuesBinding.apply {
            viewModel = quesViewModel
            lifecycleOwner = this@QuesActivity
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        quesViewModel.getQuestions.observe(this, Observer {
            id = it.id
        })

        quesViewModel.answeredList.observe(this, Observer { answers ->

            if (
                answers[0].ans.isNotEmpty() &&
                answers[1].ans.isNotEmpty() &&
                answers[2].ans.isNotEmpty() &&
                answers[3].ans.isNotEmpty() &&
                answers[4].ans.isNotEmpty()
            ) {
                val map = hashMapOf<String, String>()
                map["Question1_answer"] = answers[0].ans
                map["Question2_answer"] = answers[1].ans
                map["Question3_answer"] = answers[2].ans
                map["Question4_answer"] = answers[3].ans
                map["Question5_answer"] = answers[4].ans

                Logger.getLogger("QuesActivity:").warning("Requesting................")

                showProgressDialog().show()

                mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

                mainViewModel.getResponse(map).observe(this, Observer {

                    FirebaseRepo.submitResultData(
                        SelfResult(
                            user.email?.substring(1, 12).toString(),
                            mLocation,
                            it.response,
                            map
                        )
                    )

                    showProgressDialog().cancel()

                    val intent = Intent(this, SOSActivity::class.java)
                    intent.putExtra(SOSActivity.EXTRA_RESULT, it.response)
                    startActivity(intent)
                    finish()
                })
            }

        })
    }

    private fun showProgressDialog(): ProgressDialog {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait! Getting your report!")
        progressDialog.setCancelable(false)

        return progressDialog
    }

    override fun onBackPressed() {
        if (id < 2) showDialog()
        else quesViewModel.setPreviousQues(id)
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(this)
            .setMessage("Want to exit from this page?")
            .setCancelable(false)
            .setPositiveButton("Cancel!") { dialogInterface, i ->
                dialogInterface.dismiss()
            }.setNeutralButton("Exit!") { dialogInterface, i ->
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
}