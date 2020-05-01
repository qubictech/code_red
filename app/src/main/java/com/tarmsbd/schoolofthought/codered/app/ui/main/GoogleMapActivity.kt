package com.tarmsbd.schoolofthought.codered.app.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.data.repository.FirebaseRepo
import com.tarmsbd.schoolofthought.codered.app.ui.auth.AuthActivity
import com.tarmsbd.schoolofthought.codered.app.ui.emergency.EmergencyActivity
import com.tarmsbd.schoolofthought.codered.app.ui.ques.QuesActivity
import com.tarmsbd.schoolofthought.codered.app.ui.report.ReportActivity
import java.util.logging.Logger


class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    companion object {
        const val PERMISSION_ID = 42
    }

    private val user = FirebaseAuth.getInstance().currentUser

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut()
            recreate()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_map)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        FirebaseRepo.getReportResults { error, list ->
            Logger.getLogger("MapActivity")
                .warning("Response: Error: $error \nResult Count: ${list.size}")

            if (!error) {
                for (result in list) {
                    if (result.result == "Ref") {
                        multipleMurkerOrange(
                            result.location.latitude,
                            result.location.longitude,
                            result.result
                        )
                    } else if (result.result == "Orange") {
                        multipleMurkerRed(
                            result.location.latitude,
                            result.location.longitude,
                            result.result
                        )
                    }
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        recreate()
    }

    override fun onMapReady(googleMap: GoogleMap) {


        if (checkPermissions()) {
            if (isLocationEnabled()) {


                mMap = googleMap

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->

                        // Got last known location. In some rare situations this can be null.
                        if (location == null) {
                            val mylatlong = LatLng(23.7536267, 90.376229)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylatlong, 16f))
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(mylatlong)
                                    .title("Dhaka")
                            )
                        } else {

                            val mylatlong = LatLng(location!!.latitude, location.longitude)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylatlong, 16f))
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(mylatlong)
                                    .title("My Location")
                            )
                        }
                    }

                //Multiple marker add
                multipleMurkerRed(24.323830, 90.172589, "Red Zone")
                multipleMurkerOrange(24.315745, 90.173242, "Orange Zone")


            } else {
                //location enable

                buildAlertMessageNoGps()
            }
        } else {
            requestPermissions()
        }
    }

    fun multipleMurkerRed(lat: Double, long: Double, title: String) {
        val latLng = LatLng(lat, long)
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(bitmapDescriptorFromVector(applicationContext, R.drawable.red_signal))

        )


    }

    fun multipleMurkerOrange(lat: Double, long: Double, title: String) {
        val latLng = LatLng(lat, long)
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(bitmapDescriptorFromVector(applicationContext, R.drawable.orange))

        )


    }

    fun gotoSelfRegPage(view: View) {
        if (user == null) {
            val intent = Intent(this, AuthActivity::class.java)
            intent.putExtra(AuthActivity.EXTRA_TEXT, "QuesActivity")
            startActivity(intent)
        } else startActivity(Intent(this, QuesActivity::class.java))
    }

    fun gotoOtherHelpPage(view: View) {
        if (user == null) {
            val intent = Intent(this, AuthActivity::class.java)
            intent.putExtra(AuthActivity.EXTRA_TEXT, "ReportActivity")
            startActivity(intent)
        } else startActivity(Intent(this, ReportActivity::class.java))
    }

    fun gotoEmergencyPage(view: View) {
        startActivity(Intent(this, EmergencyActivity::class.java))
    }


    private fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return fromBitmap(bitmap)
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                    finish()
                })
        val alert: AlertDialog = builder.create()
        alert.show()
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
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }

}
