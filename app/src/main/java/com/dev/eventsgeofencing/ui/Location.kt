package com.dev.eventsgeofencing.ui

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.dev.eventsgeofencing.R
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dev.eventsgeofencing.geofencing.GeofenceRegistrationService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions

class Location : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener,
    GoogleApiClient.ConnectionCallbacks, ResultCallback<Status>,
    GoogleApiClient.OnConnectionFailedListener {

    private val GEOFENCE_ID = "Geofence"
    private val NOTIFICATION_RESPONSIVENESS = 1000
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var namaEvent: String
    private val radius = 1000f
    private lateinit var googleApiClient: GoogleApiClient
    private val REQUEST_LOCATION_PERMISSION_CODE = 101
    private var pendingIntent: PendingIntent? = null
    private var markerOptions: MarkerOptions? = null
    private var TAG: String = Location::class.java.simpleName
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mGeofencingClient: GeofencingClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_location)
        initGMaps()

        val i = intent
        latitude = i.getStringExtra("latitude")
        longitude = i.getStringExtra("longitude")
        namaEvent = i.getStringExtra("namaEvent")
    }

    private fun initGMaps() {
        mGeofencingClient = LocationServices.getGeofencingClient(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), REQUEST_LOCATION_PERMISSION_CODE
                )
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        googleMap?.uiSettings?.isZoomControlsEnabled = true
        googleMap?.setMinZoomPreference(15f)
        googleMap?.isMyLocationEnabled = true
        googleMap?.setOnMapClickListener(this)

        googleMap?.addMarker(
            MarkerOptions().position(
                LatLng(
                    latitude.toDouble(),
                    longitude.toDouble()
                )
            ).title("Location")
        )
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latitude.toDouble(),
                    longitude.toDouble()
                ), 15f
            )
        )
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL

        googleMap?.addCircle(
            CircleOptions()
                .center(LatLng(latitude.toDouble(), longitude.toDouble()))
                .radius(radius.toDouble())
                .strokeColor(Color.RED)
                .fillColor(Color.argb(20, 0, 255, 0))
                .zIndex(55f)
                .strokeWidth(6f)
        )

        googleMap?.addPolygon(
            PolygonOptions()
                .add(
                    LatLng(32.974508, -97.334948),
                    LatLng(32.973239, -97.329873),
                    LatLng(32.970606, -97.332519),
                    LatLng(32.970785, -97.335965)
                )
                .strokeColor(Color.RED)
                .strokeWidth(7f)
                .fillColor(Color.argb(25, 255, 0, 0))
        )

        googleMap?.addPolygon(
            PolygonOptions()
                .add(
                    LatLng(32.772467, -96.618180),
                    LatLng(32.773177, -96.610702),
                    LatLng(32.769388, -96.610638),
                    LatLng(32.767913, -96.614325)
                )
                .strokeColor(Color.RED)
                .strokeWidth(7f)
                .fillColor(Color.argb(25, 255, 0, 0))
        )
    }

    private fun startLocationMonitor() {
        Log.d(TAG, "start location monitor")

        try {
            mFusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                if (location != null) {
                    markerOptions = MarkerOptions()
                    markerOptions?.position(LatLng(location.latitude, location.longitude))
                    markerOptions?.title(getString(R.string.current_loc_txt))
                    Log.d(
                        TAG,
                        "Location Change Lat Lng" + location.latitude + "" + location.longitude
                    )
                }
            }
        } catch (e: SecurityException) {
            Log.d(TAG, e.message.toString())
        }
    }

    private fun startGeofencing() {
        Log.d(TAG, "Start geofencing monitoring call")
        pendingIntent = getGeofencingPendingIntent()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER)
            .addGeofence(getGeofence())
            .build()

        if (!googleApiClient.isConnected) {
            Log.d(TAG, "Google Api Client is not Connected")
        } else {
            try {
                mGeofencingClient!!.addGeofences(geofencingRequest, pendingIntent)
                    .addOnSuccessListener { Log.d(TAG, "Successfully Geofencing Connected") }
                    .addOnFailureListener { e ->
                        Log.d(TAG, "Failed to add Geofencing " + e.message)
                    }
            } catch (e: SecurityException) {
                Log.d(TAG, e.message)
            }
        }
    }

    private fun getGeofence(): Geofence? {
        return Geofence.Builder()
            .setRequestId(GEOFENCE_ID)
            .setCircularRegion(latitude.toDouble(), longitude.toDouble(), radius)
            .setNotificationResponsiveness(NOTIFICATION_RESPONSIVENESS)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()
    }

    private fun getGeofencingPendingIntent(): PendingIntent? {
        if (pendingIntent != null) {
            return pendingIntent
        }
        val intent = Intent(this, GeofenceRegistrationService::class.java)
        intent.putExtra("name", namaEvent)
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onPause() {
        super.onPause()
        val response: Int = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (response != ConnectionResult.SUCCESS) {
            Log.d(TAG, "Google Play Service Not Available")
            GoogleApiAvailability.getInstance().getErrorDialog(this, response, 1).show()
        } else {
            Log.d(TAG, "Google play service available")
        }
    }

    override fun onStart() {
        super.onStart()
        googleApiClient.reconnect()
    }

    override fun onStop() {
        super.onStop()
        googleApiClient.disconnect()
    }

    override fun onResult(status: Status) {
        Log.i(TAG, "onResult: " + status)
    }

    override fun onConnected(bundle: Bundle?) {
        startGeofencing()
        startLocationMonitor()
    }

    override fun onConnectionSuspended(i: Int) {
        Log.d(TAG, "Google Connection Suspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e(TAG, "Connection Failed:" + connectionResult.errorMessage)
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {

    }

    override fun onMapClick(latLng: LatLng?) {
        Log.i(TAG, "latLog:" + latLng.toString())
    }
}
