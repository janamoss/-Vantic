package com.example.vanticproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vanticproject.databinding.ActivityBookingHistoryBinding
import com.example.vanticproject.databinding.ActivityProfileMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingHistoryActivity : AppCompatActivity() {
    private lateinit var bindingHistory : ActivityBookingHistoryBinding
    lateinit var session: SessionManager
    var bookinghistorylist  = arrayListOf<Historybooking>()
    var serv = VanticAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingHistory = ActivityBookingHistoryBinding.inflate(layoutInflater)
        setContentView(bindingHistory.root)

        bindingHistory.recyclerview.adapter = HistoryAdapter(this.bookinghistorylist, applicationContext)
        bindingHistory.recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        bindingHistory.recyclerview.addItemDecoration(
            DividerItemDecoration(bindingHistory.recyclerview.getContext(),
                DividerItemDecoration.VERTICAL)
        )

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingHistory.bottomnavigation.setOnItemSelectedListener{
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
    override fun onResume() {
        super.onResume()
        callhistoryuser()
    }

    fun callhistoryuser() {
        bookinghistorylist.clear()
        session = SessionManager(applicationContext)
        val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
        serv.bookinghistory(email.toString()).enqueue(object : Callback<List<Historybooking>> {
            override fun onResponse(call: Call<List<Historybooking>>, response: Response<List<Historybooking>>) {
                response.body()?.forEach {
                    bookinghistorylist.add(Historybooking(it.bookingid, it.seat_number,it.email,it.fname,it.lname,it.phone
                        ,it.date_time,it.timetableid,it.price,it.time_start,it.time_end,it.vanid,it.registration_number,it.startname,it.endname,it.namestatus))
                }
                //// Set Data to RecyclerRecyclerView
                bindingHistory.recyclerview.adapter = HistoryAdapter(bookinghistorylist,applicationContext)
            }
            override fun onFailure(call: Call<List<Historybooking>>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "ผู้ใช้งานยังไม่มีประวัติการจอง",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}