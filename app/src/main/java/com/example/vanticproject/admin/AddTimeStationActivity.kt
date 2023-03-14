package com.example.vanticproject.admin

import VanticAPI
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.lab4u1.TimePickerFragment
import com.example.lab4u1.TimePickerFrangmentEnd
import com.example.vanticproject.*
import com.example.vanticproject.databinding.ActivityAddTimeStationBinding
import com.example.vanticproject.date_time.DatePickerFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddTimeStationActivity : AppCompatActivity() {
    private lateinit var bindingaddTimeStation : ActivityAddTimeStationBinding
    var start : Int = 0
    var end : Int = 0
    var van : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingaddTimeStation = ActivityAddTimeStationBinding.inflate(layoutInflater)
        setContentView(bindingaddTimeStation.root)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingaddTimeStation.bottomnavigation.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.nav_times -> {
                    val intent = Intent(this, TimeStationAdminActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_allcar -> {
                    val intent = Intent(this, VanAdminActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_allbooking -> {
                    val intent = Intent(this, BookingAdminActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        showDropdownstart()
        showDropdownend()
        showDropdownvan()

        bindingaddTimeStation.btnDone2.setOnClickListener {
            insertaddtimestation()
        }
    }

    fun getStation(onSuccess: (List<Station>) -> Unit, onFailure: (Throwable) -> Unit) {
        val api:VanticAPI= Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VanticAPI::class.java)
        api.allstation().enqueue(object : Callback<List<Station>> {
            override fun onResponse(call: Call<List<Station>>, response: Response<List<Station>>) {
                if (response.isSuccessful) {
                    val station = response.body()
                    if (station != null) {
                        onSuccess(station)
                    }
                } else {
                    onFailure(Throwable("Failed to fetch cities"))
                }
            }

            override fun onFailure(call: Call<List<Station>>, t: Throwable) {
                onFailure(t)
            }
        })

    }

    fun getVan(onSuccess: (List<VanD>) -> Unit, onFailure: (Throwable) -> Unit) {
        val api:VanticAPI= Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VanticAPI::class.java)
        api.allvan().enqueue(object : Callback<List<VanD>> {
            override fun onResponse(call: Call<List<VanD>>, response: Response<List<VanD>>) {
                if (response.isSuccessful) {
                    val van = response.body()
                    if (van != null) {
                        onSuccess(van)
                    }
                } else {
                    onFailure(Throwable("Failed to fetch cities"))
                }
            }

            override fun onFailure(call: Call<List<VanD>>, t: Throwable) {
                onFailure(t)
            }
        })

    }

    private fun showDropdownstart () {
        getStation(
            onSuccess = { station ->
                    val stationNames = station.map { it.name }.toTypedArray()
                    val adapter = ArrayAdapter(this, R.layout.dropdown_seatvan, stationNames)
                    bindingaddTimeStation.dropdownAdd1.setText("เลือกสถานีต้นทาง")
                    bindingaddTimeStation.dropdownAdd1.setAdapter(adapter)
                    bindingaddTimeStation.dropdownAdd1.setOnItemClickListener { parent, _, position, _ ->
                        start = parent.getItemIdAtPosition(position).toInt()
                    }
                },
            onFailure = { error ->
                Log.e("AddTimeStationActivity", "Failed to fetch Station", error)
            }
        )
    }

    private fun showDropdownend() {
        getStation(
            onSuccess = { station ->
                val stationNames = station.map { it.name }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_seatvan, stationNames)
                bindingaddTimeStation.dropdownAdd2.setText("เลือกสถานีปลายทาง")
                bindingaddTimeStation.dropdownAdd2.setAdapter(adapter)
                bindingaddTimeStation.dropdownAdd2.setOnItemClickListener { parent, _, position, _ ->
                    end = parent.getItemIdAtPosition(position).toInt()
                }
            },
            onFailure = { error ->
                Log.e("AddTimeStationActivity", "Failed to fetch Station", error)
            }
        )
    }

    private fun showDropdownvan() {
        getVan(
            onSuccess = { vans ->
                val VanNames = vans.map { it.vanid }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_seatvan, VanNames)
                bindingaddTimeStation.dropdownVan2.setText("เลือกรถคันที่")
                bindingaddTimeStation.dropdownVan2.setAdapter(adapter)
                bindingaddTimeStation.dropdownVan2.setOnItemClickListener { parent, _, position, _ ->
                    van = (parent.getItemAtPosition(position) as Integer).toString()
                }
            },
            onFailure = { error ->
                Log.e("AddTimeStationActivity", "Failed to fetch Vans", error)
            }
        )
    }

    fun showTimePickerDialogStart(v:View) {
        val newTimeFragment = TimePickerFragment()
        newTimeFragment.show(supportFragmentManager, "Time Picker")
    }

    fun showTimePickerDialogEnd(v:View) {
        val newTimeFragment = TimePickerFrangmentEnd()
        newTimeFragment.show(supportFragmentManager, "Time Picker")
    }

    fun showDatePickerDialog(v: View) {
        val newDateFragment = DatePickerFragment()
        newDateFragment.show(supportFragmentManager, "Date Picker")
    }

    fun insertaddtimestation() {
        if (start == null || end == null || bindingaddTimeStation.btnDateselect.text.toString().isEmpty() || bindingaddTimeStation.btnTime1.text.toString().isEmpty()
            || bindingaddTimeStation.btnTime2.text.toString().isEmpty() || bindingaddTimeStation.addPrice.text.toString().toInt() == null || van.toInt() == null ) {
            Toast.makeText(applicationContext,"คุณกรอกข้อมูลไม่ครบ กรุณากรอกข้อมูลให้ครบถ้วน.",
                Toast.LENGTH_SHORT).show()
        }
        var statusid = 1
        val api:VanticAPI= Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VanticAPI::class.java)
        api.addtimelist(
            start+1,
            end+1,
            bindingaddTimeStation.btnDateselect.text.toString(),
            bindingaddTimeStation.btnTime1.text.toString(),
            bindingaddTimeStation.btnTime2.text.toString(),
            bindingaddTimeStation.addPrice.text.toString().toInt(),
            van.toInt(),
            statusid
        ).enqueue(object : Callback<timeandstation> {
            override fun onResponse(call: Call<timeandstation>,
                                    response: retrofit2.Response<timeandstation>)
            {
                if (response.isSuccessful()){
                    Toast.makeText(applicationContext,"เพิ่มข้อมูลสำเร็จ",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(applicationContext,"เพิ่มข้อมูลไม่สำเร็จ กรุณาลองใหม่อีกครั้ง",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<timeandstation>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure"+
                        t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}