package com.example.vanticproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.databinding.ActivityContactUsBinding
import com.example.vanticproject.databinding.ActivityEditProfileMainBinding
import com.example.vanticproject.databinding.ActivityHowtoBuyBinding

class HowtoBuyActivity : AppCompatActivity() {
    private lateinit var bindinghowtobuy : ActivityHowtoBuyBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindinghowtobuy = ActivityHowtoBuyBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_howto_buy)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindinghowtobuy.bottomnavigation.setOnItemSelectedListener{
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
}