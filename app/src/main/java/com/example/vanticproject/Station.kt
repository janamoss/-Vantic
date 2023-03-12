package com.example.vanticproject

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Station(
    @Expose
    @SerializedName("stationid") val  stationid: Int,

    @Expose
    @SerializedName("name") val  name: String,

) {}

data class Province(

    @Expose
    @SerializedName("province") val  province: String,

) {}