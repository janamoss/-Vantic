package com.example.vanticproject

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Date
import java.sql.Time

data class timeandstation(
    @Expose
    @SerializedName("startstationid") val  startstationid: Int,

    @Expose
    @SerializedName("endstationid") val  endstationid: Int,

    @Expose
    @SerializedName("date_time") val  date_time: Date,

    @Expose
    @SerializedName("time_start") val  time_start: String,

    @Expose
    @SerializedName("time_end") val  time_end: String,

    @Expose
    @SerializedName("price") val  price: Int,

    @Expose
    @SerializedName("vanid") val  vanid: Int,

    @Expose
    @SerializedName("statustimeid") val  statustimeid: Int) {}

data class booking(

    @Expose
    @SerializedName("email") val email : String,

    @Expose
    @SerializedName("timetableid") val timetableid : Int,

    @Expose
    @SerializedName("seat_number") val seat_number : Int


) {}