package com.example.graduat

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.example.graduat.Api.LoginApiInterface
import com.example.graduat.Api.LoginData
import com.example.graduat.Api.LoginResponse
import com.example.graduat.databinding.ActivityOtpBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Otp : AppCompatActivity() {
    lateinit var binding: ActivityOtpBinding
    lateinit var loginRetrofit: Retrofit
    lateinit var loginApiService: LoginApiInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val baseUrl = "http://icinema.live/api/v1/"

        loginRetrofit =
            Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        loginApiService = loginRetrofit.create(LoginApiInterface::class.java)
        val call = loginApiService.verifyPhone(LoginData(20, 1027453029, "nova", "Android"))
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Toast.makeText(
                    applicationContext, " ${response.body()?.msg}", Toast.LENGTH_SHORT
                ).show()

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "worng   ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
        binding.otbOneEdit.requestFocus()
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.confirmOtpButton.setOnClickListener {
            var intent = Intent(this, InfoActivityPage::class.java)
            startActivity(intent)
        }
        binding.confirmOtpButton.setOnClickListener {
            if (binding.otbOneEdit.text.toString() == "1"
                && binding.otbTwoEdit.text.toString() == "2"
                && binding.otbThreeEdit.text.toString() == "3"
                && binding.otbFourEdit.text.toString() == "4"
                && binding.otbFiveEdit.text.toString() == "5"
                && binding.otbSixEdit.text.toString() == "6") {
                moveFromOtpInfo()
            }else{
                Toast.makeText(this,"OTP number isn't correct",Toast.LENGTH_LONG).show()
            }
        }

        nextEdit(binding.otbOneEdit, binding.otbTwoEdit)
        nextEdit(binding.otbTwoEdit, binding.otbThreeEdit)
        nextEdit(binding.otbThreeEdit, binding.otbFourEdit)
        nextEdit(binding.otbFourEdit, binding.otbFiveEdit)
        nextEdit(binding.otbFiveEdit, binding.otbSixEdit)
        nextEdit(binding.otbSixEdit, binding.otbOneEdit)


//        binding.otbOneEdit.setOnEditorActionListener { v, actionId, event ->
//            //actionId == EditorInfo.IME_ACTION_DONE
//            if (binding.otbOneEdit.text.toString()!=null) {
//                binding.otbTwoEdit.requestFocus() // move focus to the second EditText
//                true
//            } else {
//                false
//            }
//        }
    }

    private fun moveFromOtpInfo() {
        var intent = Intent(this, InfoActivityPage::class.java)
        startActivity(intent)
    }

    fun nextEdit(editText: EditText, nextEditText: EditText) {
        editText.doAfterTextChanged {
            if (binding.otbOneEdit.text.toString() != null && !binding.otbOneEdit.text.toString()
                    .equals("")
            ) {
                nextEditText.requestFocus()
            }
            if (R.id.otb_six_edit == editText.id && binding.otbOneEdit.text.toString() != null && !binding.otbOneEdit.text.toString()
                    .equals("")
            ) {
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                // TODO("want to change to nextEditText")//
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
        }
    }

}