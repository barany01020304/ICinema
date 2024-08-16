package com.example.graduat.fragment

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.graduat.R
import com.example.graduat.database.PersonalData
import com.example.graduat.databinding.FragmentMyProfileBinding
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


class MyProfileFragment : Fragment() {
    lateinit var binding: FragmentMyProfileBinding
    lateinit var config: RealmConfiguration
    lateinit var realm: Realm
    lateinit var personlData: RealmResults<PersonalData>
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var lat = 0.0
    var lon = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        config = RealmConfiguration.create(schema = setOf(PersonalData::class))
        realm = Realm.open(config)
        personlData = realm.query<PersonalData>().find()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewData()
        binding.cityCointainer.setOnClickListener {
            checkLocationSettings()
            getLocation()
        }
        binding.penChangeNameImage.setOnClickListener {
            edit(binding.fullNameTextView, binding.fullNameEditText)
        }
        binding.penChangePhoneImage.setOnClickListener {
            edit(binding.phoneTextView, binding.phoneEditText)
        }
        binding.penChangeAgeImage.setOnClickListener {
            edit(binding.ageTextView, binding.ageEditText)
        }

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

    }

    fun edit(textView: TextView, editText: EditText): String {
        textView.visibility = View.GONE
        editText.visibility = View.VISIBLE
        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                editText.visibility = View.GONE
                realm.writeBlocking {
                    if (textView.id == binding.fullNameTextView.id) {
                        findLatest(personlData[0])?.fullName = editText.text.toString()

                    }
                    if (textView.id == binding.ageTextView.id) {
                        findLatest(personlData[0])?.age = editText.text.toString().toInt()

                    }
                    if (textView.id == binding.phoneTextView.id) {
                        findLatest(personlData[0])?.phone = editText.text.toString()

                    }
                }
                textView.text = editText.text

                textView.visibility = View.VISIBLE

                true
            } else {
                editText.visibility = View.VISIBLE

                false
            }
        }


        return editText.text.toString()
    }

    fun setValueAfter() {}
    fun setViewData() {
        binding.fullNameTextView.text = personlData[0].fullName
        binding.phoneTextView.text = personlData[0].phone
        binding.ageTextView.text = personlData[0].age.toString()
        binding.cityTextView.text = personlData[0].adress
        binding.profilePhoto.setImageURI(personlData[0].accountPic.toUri())


        //  Toast.makeText(this,personlData[0].gender.toString(),Toast.LENGTH_LONG).show()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            realm.writeBlocking {
                findLatest(personlData[0])?.accountPic = uri.toString()
            }
            binding.profilePhoto.setImageURI(personlData[0].accountPic.toUri())

            // Use Uri object instead of File to avoid storage permissions
            //   imgProfile.setImageURI(fileUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this.requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this.requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this.requireContext(), "click city again", Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                200
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

        val client = LocationServices.getSettingsClient(this.requireActivity())
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
                        this.requireActivity(), 2
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    fun getCityName(lat: Double, lon: Double): String {
        val geocoder = Geocoder(this.requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        var lastCity = ""
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            val city = address.locality
            //  Toast.makeText(this,city,Toast.LENGTH_LONG).show()
            //  binding.getCityProgress.visibility = View.VISIBLE
            if (lastCity == "") {
                //  binding.getCityProgress.visibility = View.GONE
                lastCity = city
                // return city

            }
            //  return city

        }
        return lastCity
    }


}