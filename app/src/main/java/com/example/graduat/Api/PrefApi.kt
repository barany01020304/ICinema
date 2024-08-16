package com.example.graduat.Api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET





data class Responsee (
    @SerializedName("key")
     val key: String?,

    @SerializedName("msg")
     val msg: String?,

    @SerializedName("data")
     val data: List<Category>?
)

data class Category (
    @SerializedName("id")
     val id:Int,
    @SerializedName("title")
     val title: String?,
    @SerializedName("children")
     val children: List<Child>?
)

data class Child (
    @SerializedName("id")
     val id:Int,

    @SerializedName("name")
     val name: String?)

 interface ApiInterface {
    @get:GET("categories/")
    val categories: Call<Responsee>
}

