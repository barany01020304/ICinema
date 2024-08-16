package com.example.graduat

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.graduat.database.PersonalData
import com.example.graduat.databinding.ActivityInfoPageBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import java.util.*


class InfoActivityPage : AppCompatActivity() {
    lateinit var binding: ActivityInfoPageBinding
    lateinit var config: RealmConfiguration
    lateinit var realm: Realm
    lateinit var personlData: RealmResults<PersonalData>
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var lat = 0.0
    var lon = 0.0

    // Declare a variable for storing the current location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info_page)
        supportActionBar?.hide()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        config = RealmConfiguration.create(schema = setOf(PersonalData::class))
        realm = Realm.open(config)
        personlData = realm.query<PersonalData>().find()

        binding.profilePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop() //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()


        }
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.cityCointainer.setOnClickListener {
            checkLocationSettings()
            getLocation()

        }
        binding.genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Get the selected Radio Button
            val radioButton = group.findViewById(checkedId) as RadioButton

            // on below line we are setting
            // text for our status text view.
            //  statusTV.text = radioButton.text
            realm.writeBlocking {
                findLatest(personlData[0])?.gender = radioButton.text.toString()
            }
//             val courseList: RealmResults<PersonalData> =realm.query<PersonalData>().find()
//
//            Toast.makeText(this,courseList[0].gender.toString(),Toast.LENGTH_LONG).show()

        }
        binding.registerButton.setOnClickListener {
          try {
              realm.writeBlocking {
                  findLatest(personlData[0])?.fullName = binding.fullNameEditText.text.toString()
                  findLatest(personlData[0])?.age = binding.ageEditText.text.toString().toInt()
                  findLatest(personlData[0])?.adress = binding.cityTextView.text.toString()
                  findLatest(personlData[0])?.lat = lat
                  findLatest(personlData[0])?.lon = lon


              }
          }catch (e:java.lang.Exception){
              Toast.makeText(this,"Enter all data and enter it correct",Toast.LENGTH_SHORT).show()
          }
            val intent = Intent(this, PrefActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            binding.profilePhoto.setImageURI(uri)
            realm.writeBlocking {
                findLatest(personlData[0])?.accountPic = uri.toString()
            }

            //  binding.profilePhoto.setImageURI(personlData[0].accountPic.toUri())

            // Use Uri object instead of File to avoid storage permissions
            //   imgProfile.setImageURI(fileUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "click city again", Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 200
            )
            return
        }
        var location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if (it != null) {
                lat = it.latitude
                lon = it.longitude
                getCityName(it.latitude, it.longitude)
                // binding.cityTextView.text="lat ${it.latitude.toString()}, lon ${it.longitude.toString()}"
                binding.cityTextView.text = getCityName(it.latitude, it.longitude)

            }
            if (it == null) {
                binding.cityTextView.text = "null"
            }
        }
    }

    fun checkLocationSettings() {
        val locationRequest = LocationRequest.create()
        //  locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // All location settings are satisfied. The client can initialize
            // location requests here.
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        this, 2
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    fun getCityName(lat: Double, lon: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        var lastCity = ""
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            val city = address.locality
            //  Toast.makeText(this,city,Toast.LENGTH_LONG).show()
            binding.getCityProgress.visibility = View.VISIBLE
            if (lastCity == "") {
                binding.getCityProgress.visibility = View.GONE
                lastCity = city
                // return city

            }
            //  return city

        }
        return lastCity
    }

}