package com.dev.eventsgeofencing.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.dev.eventsgeofencing.R
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_location.*

class Location : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var permissionManager: PermissionsManager
    private lateinit var mapBoxMap: MapboxMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.default_public_api_key))
        Mapbox.setAccessToken(getString(R.string.default_public_api_key))
        setContentView(R.layout.activity_location)
        initMapView(savedInstanceState)
        initPermissions()
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapBoxMap = mapboxMap
        this.mapBoxMap.setStyle(Style.MAPBOX_STREETS)
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        maps_view.onCreate(savedInstanceState)
    }

    private fun initPermissions() {
        val permissionListener = object : PermissionsListener {
            override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {

            }

            override fun onPermissionResult(granted: Boolean) {
                if (granted) {
                    syncMapbox()
                } else {
                    val alertDialog = AlertDialog.Builder(this@Location)
                        .setTitle("Info")
                        .setCancelable(false)
                        .setMessage("Permission Denied")
                        .setPositiveButton("Dismiss") { _, _ ->
                            finish()
                        }
                        .create()
                    alertDialog.show()
                }
            }
        }

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            syncMapbox()
        } else {
            permissionManager = PermissionsManager(permissionListener)
            permissionManager.requestLocationPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun syncMapbox() {
        maps_view.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        maps_view.onStart()
    }

    override fun onResume() {
        super.onResume()
        maps_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        maps_view.onPause()
    }

    override fun onStop() {
        super.onStop()
        maps_view.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        maps_view.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        maps_view.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        maps_view.onSaveInstanceState(outState)
    }
}
