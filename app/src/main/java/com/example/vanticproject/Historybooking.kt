package com.example.vanticproject

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Historybooking(

        @Expose
        @SerializedName("bookingid") val  bookingid: Int,

        @Expose
        @SerializedName("seat_number") val  seat_number: Int,

        @Expose
        @SerializedName("email") val  email: String,

        @Expose
        @SerializedName("fname") val  fname: String,

        @Expose
        @SerializedName("lname") val  lname: String,

        @Expose
        @SerializedName("phone") val phone:String,

        @Expose
        @SerializedName("date_time") val  date_time: String,

        @Expose
        @SerializedName("timetableid") val  timetableid: Int,

        @Expose
        @SerializedName("price") val  price: Int,

        @Expose
        @SerializedName("time_start") val  time_start: String,

        @Expose
        @SerializedName("time_end") val  time_end: String,

        @Expose
        @SerializedName("vanid") val  vanid: Int,

        @Expose
        @SerializedName("registration_number") val  registration_number: String,

        @Expose
        @SerializedName("startname") val  startname: String,

        @Expose
        @SerializedName("endname") val  endname: String,

        @Expose
        @SerializedName("namestatus") val  namestatus: String

        ) {}
