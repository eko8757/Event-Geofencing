package com.dev.eventsgeofencing.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dev.eventsgeofencing.R
import com.dev.eventsgeofencing.notification.BaseReminder
import com.dev.eventsgeofencing.notification.Reminder
import com.dev.eventsgeofencing.utils.showReminderInMap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_google_location.*

class GoogleLocation : BaseReminder(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var map: GoogleMap? = null
    private lateinit var locationManager: LocationManager
    private lateinit var latitude: String
    private lateinit var longitude: String
    private var reminder = Reminder(latLng = null, radius = null, message = null)

    companion object {
        private const val MY_LOCATION_REQUEST_CODE = 329
        private const val NEW_REMINDER_REQUEST_CODE = 330
        private const val EXTRA_LAT_LNG = "EXTRA_LAT_LNG"

        fun newIntent(context: Context, latLng: LatLng): Intent {
            val intent = Intent(context, GoogleLocation::class.java)
            intent.putExtra(EXTRA_LAT_LNG, latLng)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_location)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_LOCATION_REQUEST_CODE
            )
        }

        //for put extra data lat & lng from detail activity
        val i = intent
        latitude = i.getStringExtra("latitude")
        longitude = i.getStringExtra("longitude")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.run {
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isMapToolbarEnabled = false
            setOnMarkerClickListener(this@GoogleLocation)
        }
        onMapPermissionReady()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return true
    }

    @SuppressLint("RestrictedApi")
    private fun onMapPermissionReady() {
        if (map != null
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            drawCircle(LatLng(latitude.toDouble(), longitude.toDouble()))
            map?.isMyLocationEnabled = true
            currentLocation.visibility = View.VISIBLE

            //go to current device locations
            currentLocation.setOnClickListener {
                val bestProvider = locationManager.getBestProvider(Criteria(), false)
                val location = locationManager.getLastKnownLocation(bestProvider)
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
                }
            }
            centerCamera()
        }
    }

    //for add radius in target locations
    private fun drawCircle(point: LatLng) {
        val valueRadius = getRadius(1000)
        reminder.radius = valueRadius
        reminder.latLng = point
        reminder.message = "Sudah sampai tujuan!"
        showReminderUpdate(reminder)
    }

    private fun showReminderUpdate(reminder: Reminder) {
        map?.clear()
        showReminderInMap(this, map!!, reminder)
    }

    private fun getRadius(radius: Int) : Double {
        return radius.toDouble()
    }

    private fun centerCamera() {
        if (intent.extras != null && intent.extras!!.containsKey(EXTRA_LAT_LNG)) {
            val latLng = intent.extras!!.get(EXTRA_LAT_LNG) as LatLng
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            onMapPermissionReady()
        }
    }
}
