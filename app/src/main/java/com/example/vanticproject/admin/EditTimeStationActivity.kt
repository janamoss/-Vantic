package com.example.vanticproject.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.lab4u1.TimePickerFragment
import com.example.lab4u1.TimePickerFrangmentEnd
import com.example.vanticproject.*
import com.example.vanticproject.databinding.ActivityEditTimeStationBinding
import com.example.vanticproject.databinding.ActivityEditVanBinding
import com.example.vanticproject.databinding.ActivityProfileMainBinding
import com.example.vanticproject.date_time.DatePickerFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class EditTimeStationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditTimeStationBinding
    lateinit var session: SessionManager
    var serv = VanticAPI.create()
    var start : Int = 0
    var end : Int = 0
    var van : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTimeStationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(applicationContext)

        showDropdownstart()
        showDropdownend()
        showDropdownvan()

        var data = intent
        var dates = data.getStringExtra("date").toString()
        var id = data.getIntExtra("id",0)
        var registration_number = data.getStringExtra("registration_number").toString()
        var time_start = data.getStringExtra("time_start").toString()
        var time_end = data.getStringExtra("time_end").toString()
        var seat = data.getIntExtra("seat",0)
        var price = data.getIntExtra("price",0)
        var statusname = data.getStringExtra("statusname").toString()

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dates)

        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()

        val outputDateString = outputFormat.format(date)

        binding.editPrice.setText(price.toString())
        binding.btnDateselect.setText(outputDateString)
        binding.btnTime1.setText(time_start)
        binding.btnTime2.setText(time_end)

        binding.btnDone.setOnClickListener {
            updatetimetable()
        }
        binding.btnReset.setOnClickListener {
            val intent = Intent(applicationContext, TimeStationAdminActivity::class.java)
            startActivity(intent)
        }
    }

    fun getStation(onSuccess: (List<Station>) -> Unit, onFailure: (Throwable) -> Unit) {
        serv.allstation().enqueue(object : Callback<List<Station>> {
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
        serv.allvan().enqueue(object : Callback<List<VanD>> {
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
                var data = intent
                var startname = data.getStringExtra("startname").toString()

                val stationNames = station.map { it.name }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_seatvan, stationNames)
                binding.dropdownEdit1.setText(startname)
                binding.dropdownEdit1.setAdapter(adapter)
                binding.dropdownEdit1.setOnItemClickListener { parent, _, position, _ ->
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
                var data = intent
                var endname = data.getStringExtra("endname").toString()
                val stationNames = station.map { it.name }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_seatvan, stationNames)
                binding.dropdownEdit2.setText(endname)
                binding.dropdownEdit2.setAdapter(adapter)
                binding.dropdownEdit2.setOnItemClickListener { parent, _, position, _ ->
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
                var data = intent
                var vanid = data.getIntExtra("vanid",0)
                val VanNames = vans.map { it.vanid }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_seatvan, VanNames)
                binding.dropdownVanadd.setText(vanid.toString())
                binding.dropdownVanadd.setAdapter(adapter)
                binding.dropdownVanadd.setOnItemClickListener { parent, _, position, _ ->
                    van = (parent.getItemAtPosition(position) as Integer).toString()
                }
            },
            onFailure = { error ->
                Log.e("AddTimeStationActivity", "Failed to fetch Vans", error)
            }
        )
    }

    fun showTimePickerDialogStart(v: View) {
        val newTimeFragment = TimePickerFragment()
        newTimeFragment.show(supportFragmentManager, "Time Picker")
    }

    fun showTimePickerDialogEnd(v: View) {
        val newTimeFragment = TimePickerFrangmentEnd()
        newTimeFragment.show(supportFragmentManager, "Time Picker")
    }

    fun showDatePickerDialog(v: View) {
        val newDateFragment = DatePickerFragment()
        newDateFragment.show(supportFragmentManager, "Date Picker")
    }

    private fun updatetimetable() {
        var statusid = 0
        var data = intent
        var id = data.getIntExtra("id",0)
        var statusname = data.getStringExtra("statusname").toString()
        if (statusname == "รถยังไม่ออกเดินทาง") {
            statusid = 1
        } else {
            statusid = 2
        }
        serv.updatetimetable(
            id,
                    start+1,
            end+1,
            binding.btnDateselect.text.toString(),
            binding.btnTime1.text.toString(),
            binding.btnTime2.text.toString(),
            binding.editPrice.text.toString().toInt(),
            van.toInt(),
            statusid
        ).enqueue(object : Callback<timestationshow> {
            override fun onResponse(call: Call<timestationshow>,
                                    response: retrofit2.Response<timestationshow>)
            {
                if (response.isSuccessful()){
                    Toast.makeText(applicationContext,"อัพเดตข้อมูลสำเร็จ",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<timestationshow>, t: Throwable) {
                Toast.makeText(applicationContext,"อัพเดตข้อมูลไม่สำเร็จ"+
                        t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}