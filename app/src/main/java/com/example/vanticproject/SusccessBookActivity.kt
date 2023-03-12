package com.example.vanticproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.databinding.ActivityBookingReserveBinding
import com.example.vanticproject.databinding.ActivityShowTimeStationBinding
import com.example.vanticproject.databinding.ActivitySusccessBookBinding

class SusccessBookActivity : AppCompatActivity() {
    private lateinit var bindingsbook: ActivitySusccessBookBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingsbook = ActivitySusccessBookBinding.inflate(layoutInflater)
        setContentView(bindingsbook.root)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingsbook.bottomnavigation.setOnItemSelectedListener{
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
        var vanid = data.getStringExtra("vanid").toString()
        var name = data.getStringExtra("name").toString()
        var email = data.getStringExtra("email").toString()
        var phone = data.getStringExtra("phone").toString()
        var user_seat = data.getIntExtra("user_seat",0)
        var time_start = data.getStringExtra("time_start").toString()
        var time_end = data.getStringExtra("time_end").toString()
        var registration_number = data.getStringExtra("registration_number").toString()
        var price = data.getIntExtra("price",0)

        bindingsbook.name.text = name
        bindingsbook.mail.text = email
        bindingsbook.tel.text = phone
        bindingsbook.vannum.text = "รถคันที่ "+vanid
        bindingsbook.vanregister.text = " เลขทะเบียน "+registration_number
        bindingsbook.start.text = "เวลารถออก : ${time_start} น."
        bindingsbook.end.text = "เวลาถึงที่หมาย : ${time_end} น."
        bindingsbook.seat.text = "จำนวนที่นั่งที่จอง : ${user_seat} ที่นั่ง"
        bindingsbook.price.text = "ราคา ${price} บาท/ที่นั่ง"
        bindingsbook.qty.text = user_seat.toString()
        bindingsbook.pricesame.text = "฿ ${price}.00"
        bindingsbook.pricesum.text = "฿ ${user_seat*price}.00"

        bindingsbook.back.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}