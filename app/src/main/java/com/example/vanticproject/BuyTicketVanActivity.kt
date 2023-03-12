package com.example.vanticproject

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
import com.example.vanticproject.databinding.ActivityBuyTicketVanBinding
import com.example.vanticproject.date_time.DatePickerForBuyTicket
import com.example.vanticproject.date_time.DatePickerFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BuyTicketVanActivity : AppCompatActivity() {
    private lateinit var bindingbuy: ActivityBuyTicketVanBinding
    var seat : String = ""
    var provincefrom : Int = 0
    var provinceto : Int  = 0
    var seats : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingbuy = ActivityBuyTicketVanBinding.inflate(layoutInflater)
        setContentView(bindingbuy.root)
        showDropdown()
        showDropdownProvinceFirst()
        showDropdownProvinceTo()

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingbuy.bottomnavigation.setOnItemSelectedListener{
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

    }
    private fun showDropdown () {
        bindingbuy.dropdownSeat.setText("กรุณาเลือกที่นั่ง")
        val sub = resources.getStringArray (R.array.VanSeat_array)
        val arrayAdapter = ArrayAdapter(this , R.layout.dropdown_seatvan , sub)
        bindingbuy.dropdownSeat.setAdapter(arrayAdapter)
        bindingbuy.dropdownSeat.setOnItemClickListener { parent, _, position, _ ->
            seat = parent.getItemAtPosition(position) as String
        }
    }

    fun getProvince(onSuccess: (List<Province>) -> Unit, onFailure: (Throwable) -> Unit) {
        val api:VanticAPI= Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VanticAPI::class.java)
        api.allprovince().enqueue(object : Callback<List<Province>> {
            override fun onResponse(call: Call<List<Province>>, response: Response<List<Province>>) {
                if (response.isSuccessful) {
                    val province = response.body()
                    if (province != null) {
                        onSuccess(province)
                    }
                } else {
                    onFailure(Throwable("Failed to fetch cities"))
                }
            }

            override fun onFailure(call: Call<List<Province>>, t: Throwable) {
                onFailure(t)
            }
        })

    }

    private fun showDropdownProvinceFirst () {
        getProvince(
            onSuccess = { province ->
                val stationNames = province.map { it.province }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_seatvan, stationNames)
                bindingbuy.selectedFrom.setText("จังหวัด")
                bindingbuy.selectedFrom.setAdapter(adapter)
                bindingbuy.selectedFrom.setOnItemClickListener { parent, _, position, _ ->
                    provincefrom = parent.getItemIdAtPosition(position).toInt()
                }
            },
            onFailure = { error ->
                Log.e("ActivityBuyTicketVanBinding", "Failed to fetch Station", error)
            }
        )
    }

    private fun showDropdownProvinceTo () {
        getProvince(
            onSuccess = { province ->
                val stationNames = province.map { it.province }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_seatvan, stationNames)
                bindingbuy.selectedTo.setText("จังหวัด")
                bindingbuy.selectedTo.setAdapter(adapter)
                bindingbuy.selectedTo.setOnItemClickListener { parent, _, position, _ ->
                    provinceto = parent.getItemIdAtPosition(position).toInt()
                }
            },
            onFailure = { error ->
                Log.e("ActivityBuyTicketVanBinding", "Failed to fetch Station", error)
            }
        )
    }

    fun showDatePickerDialog(v: View) {
        val newDateFragment = DatePickerForBuyTicket()
        newDateFragment.show(supportFragmentManager, "Date Picker")
    }

    fun searchTicket(v:View) {
        var data = intent
        var fname = data.getStringExtra("fname").toString()
        var lname = data.getStringExtra("lname").toString()
        var email = data.getStringExtra("email").toString()
        var phone = data.getStringExtra("phone").toString()
        val intent = Intent(applicationContext,ShowTimeStationActivity::class.java)
        intent.putExtra("provincefrom",provincefrom+1)
        intent.putExtra("provinceto",provinceto+1)
        intent.putExtra("date_time",bindingbuy.txtDate.text.toString())
        intent.putExtra("seats",seat.toInt())
        intent.putExtra("fname",fname)
        intent.putExtra("lname",lname)
        intent.putExtra("email",email)
        intent.putExtra("phone",phone)
        startActivity(intent)
    }

}