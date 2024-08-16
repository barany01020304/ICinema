package com.example.graduat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.graduat.databinding.ActivityFirstPageBinding

class FirstPage : AppCompatActivity() {
    lateinit var binding:ActivityFirstPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_first_page)
        supportActionBar?.hide()
        


        binding.registerButton.setOnClickListener {
            moveTo(PhonePage::class.java as  Class<Activity> ,this)
        }
        binding.guestButton.setOnClickListener {
            moveTo(MainActivity::class.java as  Class<Activity>,this )
        }

    }

    private fun moveTo(activity: Class<Activity>,context: Context) {

        val connectivityManager =  context.getSystemService (Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                val intent = Intent(this, activity)
                startActivity(intent)
            } else {
               Toast.makeText(this,"No Enternet connection",Toast.LENGTH_LONG).show()

            }
        }

    }


}