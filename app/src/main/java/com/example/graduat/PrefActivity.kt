package com.example.graduat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.graduat.Api.ApiInterface
import com.example.graduat.Api.Responsee
import com.example.graduat.data.PrefAdapter
import com.example.graduat.database.PersonalData
import com.example.graduat.databinding.ActivityPrefBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PrefActivity : AppCompatActivity() {
    lateinit var binding: ActivityPrefBinding
    var yellow = false
    lateinit var config: RealmConfiguration
    lateinit var realm: Realm
    lateinit var personlData: RealmResults<PersonalData>
    lateinit var retrofit:Retrofit
    lateinit var apiService:ApiInterface
    lateinit var prefListZero:MutableList<String>
    lateinit var prefListOne:MutableList<String>
    lateinit var prefListTwo:MutableList<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_pref)

        supportActionBar?.hide()
        setDataPref()
        config = RealmConfiguration.create(schema = setOf(PersonalData::class))
        realm = Realm.open(config)
        personlData = realm.query<PersonalData>().find()

        // makeAllFalse()

        binding.getstartedBtn.setOnClickListener {

             val intent= Intent(this,MainActivity::class.java)
             startActivity(intent)
        }


    }

    @SuppressLint("ResourceAsColor")
    fun setDataPref() {
        retrofit = Retrofit.Builder().baseUrl("http://icinema.live/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        apiService = retrofit.create(ApiInterface::class.java)
        val call = apiService.categories
        call?.enqueue(object : Callback<Responsee?> {
            override fun onResponse(call: Call<Responsee?>, response: Response<Responsee?>) {

                prefListZero = arrayListOf("")
                prefListOne = arrayListOf("")
                prefListTwo = arrayListOf("")
                prefListZero.clear()
                prefListOne.clear()
                prefListTwo.clear()
                binding.zeroTextView.text=response.body()?.data?.get(0)?.title?:"wrong"
                binding.oneTextView.text=response.body()?.data?.get(1)?.title?:"wrong"
                binding.twoTextView.text=response.body()?.data?.get(2)?.title?:"wrong"

                for (i in   response.body()?.data?.get(0)?.children!!.indices){
                    prefListZero.add(response.body()?.data?.get(0)?.children!![i].name?:"wrong")
                }
                for (i in  0 until  response.body()?.data?.get(1)?.children!!.size){
                    prefListOne.add(response.body()?.data?.get(1)?.children!![i].name?:"wrong")
                }
                for (i in  0 until  response.body()?.data?.get(2)?.children!!.size){
                    prefListTwo.add(response.body()?.data?.get(2)?.children!![i].name?:"wrong")
                }
                binding.zeroGrid.adapter=
                    PrefAdapter(prefListZero,this@PrefActivity)
                binding.oneGrid.adapter=
                    PrefAdapter(prefListOne,this@PrefActivity)
                binding.twoGrid.adapter=
                    PrefAdapter(prefListTwo,this@PrefActivity)
                binding.prefProgressBar.visibility= View.GONE
            }


            override fun onFailure(call: Call<Responsee?>, t: Throwable) {
                Toast.makeText(this@PrefActivity, "${t.message}  hi", Toast.LENGTH_LONG).show()
            }


        })
        binding.zeroGrid.setOnItemClickListener { adapterView, view, i, l ->

        }

    }


}
