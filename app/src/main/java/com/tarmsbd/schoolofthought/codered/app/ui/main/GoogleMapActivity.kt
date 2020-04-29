package com.tarmsbd.schoolofthought.codered.app.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tarmsbd.schoolofthought.codered.app.R

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLng = LatLng(23.7536267, 90.376229)
        mMap.addMarker(MarkerOptions().position(latLng).title("Marker in Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
    }

    fun gotoSelfRegPage(view: View) {
        startActivity(Intent(this, RegistrationActivity::class.java))
    }

}
