package com.example.vanticproject

import VanticAPI
import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.databinding.ActivityBookingReserveBinding
import com.example.vanticproject.databinding.ActivityBuyTicketVanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookingReserveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingReserveBinding
    val createClient = VanticAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingReserveBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        binding.bottomnavigation.setOnItemSelectedListener{
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
        var tableid = data.getStringExtra("time_station_id").toString()
        var fname = data.getStringExtra("user_fname").toString()
        var lname = data.getStringExtra("user_lname").toString()
        var email = data.getStringExtra("user_email").toString()
        var phone = data.getStringExtra("user_phone").toString()
        var user_seat = data.getIntExtra("user_seat",0)
        var vanid = data.getStringExtra("time_station_vanid").toString()
        var registration_number = data.getStringExtra("time_station_registration_number").toString()
        var startname = data.getStringExtra("time_station_startname").toString()
        var endname = data.getStringExtra("time_station_endname").toString()
        var price = data.getIntExtra("time_station_price",0)
        var time_start = data.getStringExtra("time_start").toString()
        var time_end = data.getStringExtra("time_end").toString()

        binding.txtFirstname.text = fname
        binding.txtLastname.text = lname
        binding.txtEmail.text = email
        binding.txtTell.text = phone
        binding.txtVancar.text = vanid
        binding.txtTravel.text = startname+" เวลา "+time_start+" น."
        binding.txtDestination.text = endname+" เวลา "+time_end+" น."
        binding.txtRegistration.text = registration_number
        binding.txtPriceseat.text = "${price} บาท/ที่นั่ง"
        binding.txtNumseat.text = "${user_seat} ที่นั่ง"
        binding.txtTprice.text = "$${user_seat*price}.00"

        binding.btnNext.setOnClickListener {
            val mybuilder = AlertDialog.Builder(this)
            mybuilder.apply {
                setTitle("ต้องการที่จะต้องตั้วรถตู้หรือไม้")
                setMessage("ตรวจสอบข้อมูลเรียบร้อย ต้องการที่จะยืนยันการจองตั้ว ใช่หรือไม่ ?")
                setNegativeButton("ใช่") { dialog, which ->
                    createClient.addbooking(
                    binding.txtEmail.text.toString(),
                    tableid.toInt(),
                    user_seat
                ).enqueue(object : Callback<booking> {
                        override fun onResponse(call: Call<booking>, response: Response<booking>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                applicationContext, "จองตั้วเสร็จสิ้น",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(applicationContext,SusccessBookActivity::class.java)
                            intent.putExtra("name",fname+lname)
                            intent.putExtra("email",email)
                            intent.putExtra("phone",phone)
                            intent.putExtra("vanid",vanid)
                            intent.putExtra("registration_number",registration_number)
                            intent.putExtra("time_start",time_start)
                            intent.putExtra("time_end",time_end)
                            intent.putExtra("price",price)
                            intent.putExtra("user_seat",user_seat)
                            startActivity(intent)
                            }
                        }
                        override fun onFailure(call: Call<booking>, t: Throwable) {
                            Toast.makeText(
                                applicationContext, "จองตั้วไม่สำเร็จ กรุณาลองใหม่อีกครั้ง",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                    finish()
                }
                setPositiveButton("ไม่") { dialog, which -> dialog.cancel()}
                show()
            }

        }

    }
}