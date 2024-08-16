package com.example.graduat.database

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class TicketData() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var movieName: String = ""
    var moviePicture: String = ""
    var cinemaName: String = ""
    var cinemaPicture: String = ""

    var starRating: String = ""
    var starring: String = ""
    var releaseDate: String = ""
    var movieType: String = ""
    var movieDescription: String = ""
    var VideoUrl: String = ""

    constructor(id: ObjectId) : this() {
        _id = id
    }

}