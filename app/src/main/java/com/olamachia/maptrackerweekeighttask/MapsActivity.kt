package com.olamachia.maptrackerweekeighttask

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.olamachia.maptrackerweekeighttask.pokemonapp.PokemonActivity
import java.util.concurrent.TimeUnit



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var map: GoogleMap

    //reference to the database
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dbReference: DatabaseReference = database.getReference("Users")

    // LocationRequest - Requirements for the location updates, i.e., how often you
    // should receive updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupLocClient()


        //initialize the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        //set image upload activity selected
        bottomNavigationView.selectedItemId = R.id.map

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.pokemon -> pokemon()
                R.id.map -> null

            }
            true
        }

    }

    private fun pokemon() {
        val intent = Intent(this, PokemonActivity::class.java)
        startActivity(intent)
    }

    private lateinit var fusedLocClient: FusedLocationProviderClient
    // use it to request location updates and get the latest location

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap //initialise map
        getCurrentLocation()
        getPartnerLocation()
    }

    private fun setupLocClient() {
        fusedLocClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

    // prompt the user to grant/deny access
    private fun requestLocPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), //permission in the manifest
            REQUEST_LOCATION
        )
    }

    companion object {
        private const val REQUEST_LOCATION =
            1 //request code to identify specific permission request
        private const val TAG = "MapsActivity" // for debugging
    }

    private fun getPartnerLocation() {
        // Check if the ACCESS_FINE_LOCATION permission was granted before requesting a location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            // call requestLocPermissions() if permission isn't granted
            requestLocPermissions()
        } else {
            dbReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        //get the exact longitude and latitude from the database "test"
                        val location =
                            snapshot.child("RotimiLocation").getValue(LocationInfo::class.java)
                        val locationLat = location?.latitude
                        val locationLong = location?.longitude
                        //trigger reading of location from database using the button

                        // check if the latitude and longitude is not null
                        if (locationLat != null && locationLong != null) {
                            // create a LatLng object from location
                            val latLng = LatLng(locationLat, locationLong)
                            //create a marker at the read location and display it on the map
                            map.addMarker(
                                MarkerOptions().position(latLng)
                                    .title("The user is currently here")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.akeem_rotimi))
                            )
                            //specify how the map camera is updated
                            val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
                            //update the camera with the CameraUpdate object
                            map.moveCamera(update)
                        } else {
                            // if location is null , log an error message
                            Log.e(TAG, "user location cannot be found")
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        applicationContext,
                        "Could not read from database",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

            })
            fusedLocClient.lastLocation.addOnCompleteListener {
                // lastLocation is a task running in the background
                val location = it.result //obtain location
                //reference to the database
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    // create a marker at the exact location
                    map.addMarker(
                        MarkerOptions().position(latLng)
                            .title("You are currently here!")
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
                        ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.akeem_rotimi))
                    // create an object that will specify how the camera will be updated
                    val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
                    map.moveCamera(update)
                    //Save the location data to the database
                } else {
                    // if location is null , log an error message
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
                    // Log.e(TAG, "No location found")
                }
            }
        }

    }

    private fun getCurrentLocation() {
        // Check if the ACCESS_FINE_LOCATION permission was granted before requesting a location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {

            // call requestLocPermissions() if permission isn't granted
            requestLocPermissions()
        } else {

            locationRequest = LocationRequest.create()
            locationRequest.apply {
                locationRequest.interval = 5000
                locationRequest.fastestInterval = 1500
                locationRequest.priority = PRIORITY_HIGH_ACCURACY
                fusedLocClient.requestLocationUpdates(
                    locationRequest, locationCallback, Looper.myLooper()!!
                )
            }

        }
    }

    private var locationCallback = object : LocationCallback() {

        override fun onLocationResult(p0: LocationResult) {
            val updatedLocation = p0.lastLocation
            val latLng = LatLng(updatedLocation.latitude, updatedLocation.longitude)
            val myFirebaseData = LocationInfo(updatedLocation.latitude, updatedLocation.longitude)
            Log.d("callback", "This is a new location: $updatedLocation")


            map.clear()
            map.addMarker(
                MarkerOptions().position(latLng)
                    .title("Stanley is here")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.me))
            )
            val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
            map.moveCamera(update)
            dbReference.child("stanley").setValue(myFirebaseData)
        }
    }


    //Creating map_option menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    //Mape_option menu options
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        //change the map type based on user's selection
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //check if the request code matches the REQUEST_LOCATION
        if (requestCode == REQUEST_LOCATION) {
            //check if grantResults contains PERMISSION_GRANTED.If it does, call getCurrentLocation()
            if (grantResults.size == 1 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                getCurrentLocation()
            } else {
                //if it doesn`t log an error message
                Log.e(TAG, "Location permission has been denied")
            }
        }
    }

}
//TODO: Push to my personal github repo



