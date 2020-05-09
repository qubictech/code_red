package com.tarmsbd.schoolofthought.codered.app.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
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
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.firebase.auth.FirebaseAuth
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.data.repository.FirebaseRepo
import com.tarmsbd.schoolofthought.codered.app.ui.auth.AuthActivity
import com.tarmsbd.schoolofthought.codered.app.ui.emergency.EmergencyActivity
import com.tarmsbd.schoolofthought.codered.app.ui.ques.QuesActivity
import com.tarmsbd.schoolofthought.codered.app.ui.report.ReportActivity
import java.io.IOException
import java.util.logging.Logger


class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private lateinit var search: EditText

    companion object {
        const val PERMISSION_ID = 42
    }

    private val user = FirebaseAuth.getInstance().currentUser

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun locationSearch() {

        var addressList: List<Address>? = null
        search = findViewById(R.id.map_search)
        val location: String = search.text.toString()

        if (location == "") {
            Toast.makeText(applicationContext, "provide location", Toast.LENGTH_SHORT).show()
        } else {
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location, 5)

            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (addressList.isNullOrEmpty()) {
                Toast.makeText(this, "Location not found", Toast.LENGTH_LONG).show()
                return
            }
            val address = addressList[0]
            val latLng = LatLng(address.latitude, address.longitude)
            mMap.addMarker(MarkerOptions().position(latLng))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
        }
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

    }

    override fun onStart() {
        super.onStart()
        if (checkPermissions()) {
            if (!isLocationEnabled()) {
                buildAlertMessageNoGps()
            }
        } else {
            requestPermissions()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.mapstyle
                )
            )
            if (!success) {
                Log.e("Map Failed----", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(" Map Error---", "Can't find style. Error: ", e)
        }

        if (checkPermissions()) {
            if (isLocationEnabled()) {

                // list of report result.
                FirebaseRepo.getReportResults { error, list ->
                    Logger.getLogger("MapActivity")
                        .warning("Response: Error: $error \nResult Count: ${list.size}")

                    if (!error) {
                        for (result in list) {
                            if (result.result == "Red") {
                                multipleMarkerRed(
                                    result.location.latitude,
                                    result.location.longitude,
                                    result.result
                                )
                            } else if (result.result == "Orange") {
                                multipleMarkerOrange(
                                    result.location.latitude,
                                    result.location.longitude,
                                    result.result
                                )
                            }
                        }
                    }
                }


                mMap = googleMap
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//                mMap.isMyLocationEnabled = true

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            mMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        location.latitude,
                                        location.longitude
                                    ), 13f
                                )
                            )
                        }
                    }
            } else {
                //location enable
                buildAlertMessageNoGps()
            }
        } else {
            requestPermissions()
        }
    }

    private fun addCircleInLocation(map: GoogleMap, latLng: LatLng) {
        map.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(300.0)/*in meter*/
                .strokeWidth(3f)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(90, 150, 50, 50))
        )
    }

    private fun multipleMarkerRed(lat: Double, long: Double, title: String) {
        val latLng = LatLng(lat, long)
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(
                    bitmapDescriptorFromVector(
                        applicationContext,
                        R.drawable.ic_pin_outline_red
                    )
                )
        )

        addCircleInLocation(mMap, LatLng(lat, long))

    }

    private fun multipleMarkerOrange(lat: Double, long: Double, title: String) {
        val latLng = LatLng(lat, long)
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(
                    bitmapDescriptorFromVector(
                        applicationContext,
                        R.drawable.ic_pin_outline_orange
                    )
                )
        )

        addCircleInLocation(mMap, LatLng(lat, long))

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
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "No"
            ) { dialog, _ ->
                dialog.cancel()
                finish()
            }
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

    fun clickSearch(view: View) {
        locationSearch()
    }

}
