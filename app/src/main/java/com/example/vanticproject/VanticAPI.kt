import com.example.vanticproject.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface VanticAPI {
    @FormUrlEncoded
    @POST("register")
    fun register(
     @Field("email") email: String,
     @Field("password") password: String,
     @Field("fname") fname: String,
     @Field("lname") lname: String,
     @Field("phone") phone: String,
     @Field("usertypeid") usertypeid: Int): Call<User>

    @GET("login/{email}/{password}")
    fun userLogin
       (
        @Path("email") email: String,
        @Path("password") password: String
    ): Call<LoginResponse>

    @GET("allvanlist")
    fun retrieveVan(): Call<List<Van>>

    @GET("allstation")
    fun allstation() : Call<List<Station>>

    @GET("allprovince")
    fun allprovince() : Call<List<Province>>

    @GET("allvan")
    fun allvan() : Call<List<VanD>>

    @GET("allstatusname")
    fun allstatus() : Call<List<Status>>

    @GET("booking/history/{email}")
    fun bookinghistory(
        @Path("email") email: String
    ): Call<List<Historybooking>>

    @GET("allbooking")
    fun allbooking() : Call<List<Historybooking>>

    @GET("alltimestation")
    fun alltimestation() : Call<List<timestationshow>>

    @GET("Search/Ticket/{starstationid}/{endstationid}/{date_time}/{seats}")
    fun searchTicket(
        @Path("starstationid") starstationid: Int,
        @Path("endstationid") endstationid: Int,
        @Path("date_time") date_time: String,
        @Path("seats") seats: Int
    ) : Call<List<timestationshow>>

    @FormUrlEncoded
    @POST("addvan")
    fun addvanlist(
        @Field("registration_number") registration_number: String,
        @Field("seats") seats: Int,
        @Field("driver") driver: String ): Call<Van>

    @FormUrlEncoded
    @POST("addbooking")
    fun addbooking(
        @Field("email") email: String,
        @Field("timetableid") timetableid: Int,
        @Field("seat_number") seat_number: Int
    ): Call<booking>

    @FormUrlEncoded
    @POST("addtimetable")
    fun addtimelist(
        @Field("startstationid") startstationid: Int,
        @Field("endstationid") endstationid: Int,
        @Field("date_time") date_time: String,
        @Field("time_start") time_start: String,
        @Field("time_end") time_end: String,
        @Field("price") price : Int,
        @Field("vanid") vanid : Int,
        @Field("statustimeid") statustimeid : Int
    ): Call<timeandstation>

    @PUT("booking/update/{statusid}/{timeid}")
    fun updatebooking(
        @Path("statusid") statusid: Int,
        @Path("timeid") timeid: Int
    ): Call<booking>

    @FormUrlEncoded
    @PUT("van/update/{vanid}")
    fun updatevan(
        @Path("vanid") vanid: Int,
        @Field("registration_number") registration_number: String,
        @Field("seats") seats: Int,
        @Field("driver") driver: String
    ): Call<Van>

    @FormUrlEncoded
    @PUT("timetable/update/{timetable}")
    fun updatetimetable(
        @Path("timetable") timetable: Int,
        @Field("startstationid") startstationid: Int,
        @Field("endstationid") endstationid: Int,
        @Field("date_time") date_time: String,
        @Field("time_start") time_start: String,
        @Field("time_end") time_end: String,
        @Field("price") price : Int,
        @Field("vanid") vanid : Int,
        @Field("statustimeid") statustimeid : Int
    ): Call<timestationshow>

    @DELETE("booking/{bookingid}")
    fun deletebooking(
        @Path("bookingid") bookingid: Int
    ): Call<booking>

    @DELETE("van/{vanid}")
    fun deletevan(
        @Path("vanid") vanid: Int
    ): Call<Van>

    @DELETE("timetable/{timetableid}")
    fun deletetimetable(
        @Path("timetableid") timetableid: Int
    ): Call<timeandstation>

    companion object {
        fun create(): VanticAPI {
            val stdClient: VanticAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VanticAPI::class.java)
            return stdClient
        }
    }
}