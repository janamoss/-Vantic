package com.example.vanticproject

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Van(

    @Expose
    @SerializedName("vanid") val  vanid: Int,

    @Expose
    @SerializedName("registration_number") val  registration_number: String,

    @Expose
    @SerializedName("seats") val  seats: Int,

    @Expose
    @SerializedName("driver") val  driver: String

) {}


data class VanD(

    @Expose
    @SerializedName("vanid") val  vanid: Int,

    @Expose
    @SerializedName("driver") val  driver: String

) {}