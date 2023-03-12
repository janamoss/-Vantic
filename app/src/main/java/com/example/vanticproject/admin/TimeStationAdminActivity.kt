package com.example.vanticproject.admin

import VanticAPI
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vanticproject.*
import com.example.vanticproject.databinding.ActivityTimeStationAdminBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TimeStationAdminActivity : AppCompatActivity() {
    private lateinit var bindingTimeStationA : ActivityTimeStationAdminBinding
    var timeandstationlist  = arrayListOf<timestationshow>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingTimeStationA = ActivityTimeStationAdminBinding.inflate(layoutInflater)
        setContentView(bindingTimeStationA.root)

        bindingTimeStationA.recyclerview.adapter = TimeStationAdapter(this.timeandstationlist, applicationContext)
        bindingTimeStationA.recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        bindingTimeStationA.recyclerview.addItemDecoration(
            DividerItemDecoration(bindingTimeStationA.recyclerview.getContext(),
                DividerItemDecoration.VERTICAL)
        )

        bindingTimeStationA.btnAddTimeStation.setOnClickListener {
            val intent = Intent(applicationContext, AddTimeStationActivity::class.java)
            startActivity(intent)
        }
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }
    }
    override fun onResume() {
        super.onResume()
        callTimestationData()
    }

    fun callTimestationData(){
        timeandstationlist.clear();
        val serv : VanticAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VanticAPI ::class.java)

        serv.alltimestation()
            .enqueue(object : Callback<List<timestationshow>> {
                override fun onResponse(call: Call<List<timestationshow>>, response: Response<List<timestationshow>>) {
                    response.body()?.forEach {
                        timeandstationlist.add(timestationshow(it.timetableid,it.startname, it.endname,it.vanid,it.registration_number,it.namestatus,it.price,it.time_start,it.time_end,it.date_time,it.seats))
                    }
                    //// Set Data to RecyclerRecyclerView
                    bindingTimeStationA.recyclerview.adapter = TimeStationAdapter(timeandstationlist,applicationContext)
                }
                override fun onFailure(call: Call<List<timestationshow>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}