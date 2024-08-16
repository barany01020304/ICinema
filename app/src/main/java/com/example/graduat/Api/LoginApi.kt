package com.example.graduat.Api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginData(
    @SerializedName("country_code") val country_code: Int,

    @SerializedName("phone") val phone: Int,
    @SerializedName("device_id") val device_id: String,
    @SerializedName("device_type") val device_type: String,

    )

data class LoginResponse(
    @SerializedName("key") val verify_phone: String, @SerializedName("msg") val msg: String
)

interface LoginApiInterface {
    @POST("login")
    fun verifyPhone(@Body data: LoginData): Call<LoginResponse>
}