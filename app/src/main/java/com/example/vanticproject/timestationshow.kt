package com.example.vanticproject

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class timestationshow(

    @Expose
    @SerializedName("timetableid") val timetableid : Int,

    @Expose
    @SerializedName("startname") val  startname: String,

    @Expose
    @SerializedName("endname") val  endname: String,

    @Expose
    @SerializedName("vanid") val  vanid: Int,

    @Expose
    @SerializedName("registration_number") val  registration_number: String,

    @Expose
    @SerializedName("namestatus") val  namestatus: String,

    @Expose
    @SerializedName("price") val  price: Int,

    @Expose
    @SerializedName("time_start") val  time_start: String,

    @Expose
    @SerializedName("time_end") val  time_end: String,

    @Expose
    @SerializedName("date_time") val  date_time: String?,

    @Expose
    @SerializedName("seats") val  seats: Int
) {}
