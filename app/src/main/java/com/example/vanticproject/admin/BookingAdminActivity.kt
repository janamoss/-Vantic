package com.example.vanticproject.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vanticproject.*
import com.example.vanticproject.databinding.ActivityBookingAdminBinding
import com.example.vanticproject.databinding.ActivityVanAdminBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingAdminActivity : AppCompatActivity() {
    private lateinit var bindingbookA: ActivityBookingAdminBinding
    var bookinglist  = arrayListOf<Historybooking>()
    var serv = VanticAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingbookA = ActivityBookingAdminBinding.inflate(layoutInflater)
        setContentView(bindingbookA.root)

        bindingbookA.recyclerview.adapter = BookingAdapter(this.bookinglist, applicationContext)
        bindingbookA.recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        bindingbookA.recyclerview.addItemDecoration(
            DividerItemDecoration(bindingbookA.recyclerview.getContext(),
                DividerItemDecoration.VERTICAL)
        )

    }

    override fun onResume() {
        super.onResume()
        callBookingdata()
    }

    fun callBookingdata(){
        bookinglist.clear();
        serv.allbooking()
            .enqueue(object : Callback<List<Historybooking>> {
                override fun onResponse(call: Call<List<Historybooking>>, response: Response<List<Historybooking>>) {
                    response.body()?.forEach {
                        bookinglist.add(Historybooking(it.bookingid, it.seat_number,it.email,it.fname,it.lname,it.phone
                            ,it.date_time,it.timetableid,it.price,it.time_start,it.time_end,it.vanid,it.registration_number,it.startname,it.endname,it.namestatus))
                    }
                    //// Set Data to RecyclerRecyclerView
                    bindingbookA.recyclerview.adapter = BookingAdapter(bookinglist,applicationContext)
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