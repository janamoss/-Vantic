package com.example.vanticproject

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Status(

    @Expose
    @SerializedName("statustimeid") val  statustimeid: Int,

    @Expose
    @SerializedName("namestatus") val  namestatus: String,

    ) {}