package com.example.vanticproject

import VanticAPI
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vanticproject.databinding.ActivityShowTimeStationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class ShowTimeStationActivity : AppCompatActivity() {
    private lateinit var bindingshowtime: ActivityShowTimeStationBinding
    var timestationlist  = arrayListOf<timestationshow>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingshowtime = ActivityShowTimeStationBinding.inflate(layoutInflater)
        setContentView(bindingshowtime.root)


        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingshowtime.bottomnavigation.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.nav_booking -> {
                    val intent = Intent(this, BuyTicketVanActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_history -> {
                    val intent = Intent(this, BookingHistoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileMainActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        var data = intent
        var seat_user = data.getIntExtra("seats",0)
        var fname = data.getStringExtra("fname").toString()
        var lname = data.getStringExtra("lname").toString()
        var email = data.getStringExtra("email").toString()
        var phone = data.getStringExtra("phone").toString()
        bindingshowtime.recyclerview.adapter = TimeStationShowAdapter(this.timestationlist,seat_user,fname,lname,email,phone, applicationContext)
        bindingshowtime.recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        bindingshowtime.recyclerview.addItemDecoration(
            DividerItemDecoration(bindingshowtime.recyclerview.getContext(),
                DividerItemDecoration.VERTICAL)
        )
        val inputFormat = SimpleDateFormat("yyyy:MM:dd", Locale.US)
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("th", "TH"))

        var dates = data.getStringExtra("date_time")

        val date = inputFormat.parse(dates)
        val formattedDate = outputFormat.format(date)
        bindingshowtime.txtdate.text = "วันที่ "+formattedDate

    }

    override fun onResume() {
        super.onResume()
        callTimeStationData()
    }

    fun callTimeStationData(){
        var data = intent
        var seat_user = data.getIntExtra("seats",0)
        var fname = data.getStringExtra("fname").toString()
        var lname = data.getStringExtra("lname").toString()
        var email = data.getStringExtra("email").toString()
        var phone = data.getStringExtra("phone").toString()
        var starstationid = intent.getIntExtra("provincefrom",0)
        var endstationid = intent.getIntExtra("provinceto",0)
        var dates = data.getStringExtra("date_time")
        var seats = intent.getIntExtra("seats",0)
        timestationlist.clear();

        if (starstationid.toString().isEmpty() || endstationid.toString().isEmpty()
            || dates.toString().isEmpty() || seats.toString().isEmpty()
             ) {
            Toast.makeText(applicationContext,"กรุณากรอกข้อมูลให้ครบถ้วนเพื่อค้นหาเที่ยวรถ",
                Toast.LENGTH_SHORT).show()
        }

        val serv : VanticAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VanticAPI ::class.java)
        serv.searchTicket(starstationid,endstationid, dates.toString(),seats )
            .enqueue(object : Callback<List<timestationshow>> {
                override fun onResponse(call: Call<List<timestationshow>>, response: Response<List<timestationshow>>) {
                    response.body()?.forEach {
                        timestationlist.add(timestationshow(it.timetableid,it.startname,it.endname,it.vanid,it.registration_number,it.namestatus,it.price,it.time_start,it.time_end,it.date_time,it.seats))
                    }
                    //// Set Data to RecyclerRecyclerView
                    bindingshowtime.recyclerview.adapter = TimeStationShowAdapter(timestationlist,seat_user,fname,lname,email,phone,applicationContext)
                }
                override fun onFailure(call: Call<List<timestationshow>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

}