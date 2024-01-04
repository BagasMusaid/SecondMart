package com.example.myapplication

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication.databinding.ActivityLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class locationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationBinding
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permisisionCode =101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //val
        getCurrentLocationUser()

    }
    private fun getCurrentLocationUser(){
        if(ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), permisisionCode)
            return
        }
        val getLocation= fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            location ->

            if(location !=null){

                currentLocation =location
                Toast.makeText(applicationContext, currentLocation.latitude.toString()+ " " +
                currentLocation.longitude.toString(), Toast.LENGTH_LONG).show()

                val mapFragment =
                    supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
                mapFragment.getMapAsync(this)

            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            permisisionCode -> if(grantResults.isNotEmpty() && grantResults[0]==
                PackageManager.PERMISSION_GRANTED){

                getCurrentLocationUser()
            }
        }
    }
    override fun onMapReady( mMap: GoogleMap) {
        val latLng =LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions=MarkerOptions().position(latLng).title("Current Location")

        mMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12F))
        mMap?.addMarker(markerOptions)

    }
}