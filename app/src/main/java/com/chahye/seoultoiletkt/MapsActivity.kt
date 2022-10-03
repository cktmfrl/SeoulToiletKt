package com.chahye.seoultoiletkt

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.content.AsyncTaskLoader

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.chahye.seoultoiletkt.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Log.d("Activity", "onCreate: ")

//        if (hasPermission()) {
//            initMap()
//        } else {
//            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_CODE)
//        }

//        binding.myLocationButton.setOnClickListener {
//            onMyLocationButtonClick()
//        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val temp = LatLng(0.0, 0.0)
        mMap.addMarker(MarkerOptions().position(temp).title("Marker"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(temp))

        val seoul = LatLng(37.56, 126.97)

        val options = MarkerOptions()
            .position(seoul)
            .title("서울")
            .snippet("한국의 수도")
        mMap.addMarker(options)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 20f))
    }


    val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val REQUEST_PERMISSION_CODE = 1

    val DEFAUT_ZOOM_LEVEL = 17f

    val CITY_HALL = LatLng(37.5662952, 126.977945099999994)

    var googleMap: GoogleMap? = null


    fun hasPermission(): Boolean {
        Log.d("Activity", "hasPermission: 1111111")

        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.d("Activity", "hasPermission: 2222222")
                return false
            }
        }

        Log.d("Activity", "hasPermission: 33333")
        return true
    }

//    @SuppressLint("MissingPermission")
//    fun initMap() {
//        binding.mapView.getMapAsync {
//            googleMap = it
//            it.uiSettings.isMyLocationButtonEnabled = false
//            when {
//                hasPermission() -> {
//                    it.isMyLocationEnabled = true
//                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(getMyLocation(), DEFAUT_ZOOM_LEVEL))
//                }
//                else -> {
//                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_HALL, DEFAUT_ZOOM_LEVEL))
//                }
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        initMap()
//    }
    
    @SuppressLint("MissingPermission")
    fun getMyLocation(): LatLng {
        val locationProvider: String = LocationManager.GPS_PROVIDER
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val lastKnownLocation: Location = locationManager.getLastKnownLocation(locationProvider)!!
        return LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
    }

    val bitmap by lazy {
        val drawable = resources.getDrawable(R.drawable.restroom_sign) as BitmapDrawable
        Bitmap.createScaledBitmap(drawable.bitmap, 64, 64, false)
    }

    fun JSONArray.merge(anotherArray: JSONArray) {
        for (i in 0 until anotherArray.length()) {
            this.put(anotherArray.get(i))
        }
    }

    fun readData(startIndex: Int, lastIndex: Int): JSONObject {
        val url =
            URL("https://openAPI.seoul.go.kr:8088/${API_KEY}/json/SearchPublicToiletPOIService/${startIndex}/${lastIndex}")
        val connection = url.openConnection()

        val data = connection.getInputStream().readBytes().toString(charset("UTF-8"))
        return JSONObject(data)
    }

    val API_KEY = "494d646756736b6b3735716b486a41"

    val task: TolietReadTask? = null
    var toilets = JSONArray()

    inner class TolietReadTask : AsyncTask<Void, JSONArray, String>() {
        override fun onPreExecute() {
            super.onPreExecute()


        }

        override fun doInBackground(vararg params: Void?): String {
            TODO("Not yet implemented")
        }


    }

}