package com.example.graduat.Api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

//data class HomeApi(
//    @SerializedName("key")
//    val key: String,
//
//    @SerializedName("msg")
//    val msg: String,
//    @SerializedName("data")
//    val data: HomeData,
//)
//data class HomeData(
//    @SerializedName("home_slides")
//    val homeSlides: HomeSlides,
//    @SerializedName("categories")
//    val categories:Categories
//)
//data class HomeSlides(
//    @SerializedName("id")
//    val id: List<Int>,
//    @SerializedName("type")
//    val type: List<String>,
//    @SerializedName("movie")
//    val movie: Movies,
//    @SerializedName("cinema")
//    val cinema: String,
//    @SerializedName("image")
//    val image: String,
//)
//
//data class Movies(
//    @SerializedName("id")
//    val id: Int,
//    @SerializedName("image")
//    val image: String,
//    @SerializedName("cover")
//    val cover: String,
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("saved")
//    val saved: String,
//    @SerializedName("cinema")
//    val cinema: Cinema,
//    @SerializedName("categories")
//    val categories: List<Categories>,
//
//)
//
//data class Cinema(
//    @SerializedName("id")
//    val id: Int,
//    @SerializedName("picture")
//    val picture: String,
//    @SerializedName("logo")
//    val logo: String,
//    @SerializedName("cover")
//    val cover: String,
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("following")
//    val following: String,
//    @SerializedName("average_rate")
//    val average_rate: Int,
//    @SerializedName("address")
//    val address: String,
//    @SerializedName("distance")
//    val distance: String,
////    @SerializedName("cinema")
////    val cinema: Cinema,
//)
//
//data class Categories(
//    @SerializedName("id")
//    val id: List<Int>,
//    @SerializedName("name")
//    val name: List<String>,
////    @SerializedName("children")
////    val children: List<Movies>,
//)
interface HomeApiInterface {
    @get:GET("home/")
    val categories: Call<HomeApi>
}
data class HomeApi (

    @SerializedName("key"  ) var key  : String? = null,
    @SerializedName("msg"  ) var msg  : String? = null,
    @SerializedName("data" ) var data : Data?   = Data()

)
data class Cinema (

    @SerializedName("id"           ) var id          : Int?    = null,
    @SerializedName("picture"      ) var picture     : String? = null,
    @SerializedName("logo"         ) var logo        : String? = null,
    @SerializedName("cover"        ) var cover       : String? = null,
    @SerializedName("name"         ) var name        : String? = null,
    @SerializedName("following"    ) var following   : String? = null,
    @SerializedName("average_rate" ) var averageRate : Int?    = null,
    @SerializedName("address"      ) var address     : String? = null,
    @SerializedName("distance"     ) var distance    : String? = null

)

data class Movie (

    @SerializedName("id"         ) var id         : Int?                  = null,
    @SerializedName("image"      ) var image      : String?               = null,
    @SerializedName("cover"      ) var cover      : String?               = null,
    @SerializedName("name"       ) var name       : String?               = null,
    @SerializedName("saved"      ) var saved      : String?               = null,
    @SerializedName("cinema"     ) var cinema     : Cinema?               = Cinema(),
    @SerializedName("categories" ) var categories : ArrayList<Categories> = arrayListOf()

)
data class HomeSlides (

    @SerializedName("id"     ) var id     : Int?    = null,
    @SerializedName("type"   ) var type   : String? = null,
    @SerializedName("movie"  ) var movie  : Movie?  = Movie(),
    @SerializedName("cinema" ) var cinema : String? = null,
    @SerializedName("image"  ) var image  : String? = null

)
data class Data (

    @SerializedName("home_slides" ) var homeSlides : ArrayList<HomeSlides> = arrayListOf(),
    @SerializedName("categories"  ) var categories : ArrayList<Categories> = arrayListOf()

)
data class Categories (

    @SerializedName("title"  ) var title  : String?           = null,
    @SerializedName("type"   ) var type   : String?           = null,
    @SerializedName("movies" ) var movies : ArrayList<Movies> = arrayListOf()

)
data class Movies (

    @SerializedName("id"         ) var id         : Int?                  = null,
    @SerializedName("image"      ) var image      : String?               = null,
    @SerializedName("cover"      ) var cover      : String?               = null,
    @SerializedName("name"       ) var name       : String?               = null,
    @SerializedName("saved"      ) var saved      : String?               = null,
    @SerializedName("cinema"     ) var cinema     : Cinema?               = Cinema(),
    @SerializedName("categories" ) var categories : ArrayList<Categories> = arrayListOf()

)