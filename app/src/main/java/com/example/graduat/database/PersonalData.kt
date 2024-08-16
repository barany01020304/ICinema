package com.example.graduat.database

import com.example.graduat.R
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class PersonalData() : RealmObject {

    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var fullName: String = ""
    var accountPic:String= R.drawable.account_pic.toString()
    var phone: String = "0"
    var age: Int = 0
    var gender: String = "nongender"
    var adress: String = "non"
    var lat:Double=0.0
    var lon:Double=0.0

    //pref//
    var adventure: Boolean = false
    var drama: Boolean = false
    var sport: Boolean = false
    var action: Boolean = false
    var thriller: Boolean = false
    var crime: Boolean = false
    var comedy: Boolean = false
    var animation: Boolean = false
    var romance: Boolean = false
    var fantasy: Boolean = false
    var scienceFiction: Boolean = false
    var family: Boolean = false
    var horror: Boolean = false
    var egyptian: Boolean = false
    var german: Boolean = false
    var japanese: Boolean = false
    var arabic: Boolean = false
    var english: Boolean = false
    var french: Boolean = false
    var chinese: Boolean = false
    var turkish: Boolean = false
    var indonesian: Boolean = false
    var spanish: Boolean = false
    var indian: Boolean = false
    var greek: Boolean = false
    var italian: Boolean = false
    var russian: Boolean = false


    constructor(id: ObjectId) : this() {
        _id = id
    }

}
