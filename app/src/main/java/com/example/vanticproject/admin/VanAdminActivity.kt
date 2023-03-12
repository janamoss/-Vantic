package com.example.vanticproject.admin

import VanticAPI
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vanticproject.R
import com.example.vanticproject.Van
import com.example.vanticproject.VanticAdapter
import com.example.vanticproject.databinding.ActivityVanAdminBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VanAdminActivity : AppCompatActivity() {
    private lateinit var bindingvan: ActivityVanAdminBinding
    var vanticvanlist  = arrayListOf<Van>()
    var mID = ""
    var serv = VanticAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingvan = ActivityVanAdminBinding.inflate(layoutInflater)
        setContentView(bindingvan.root)

        bindingvan.recyclerview.adapter = VanticAdapter(this.vanticvanlist, applicationContext)
        bindingvan.recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        bindingvan.recyclerview.addItemDecoration(
            DividerItemDecoration(bindingvan.recyclerview.getContext(),
                DividerItemDecoration.VERTICAL)
        )

        mID = intent.getStringExtra("mId").toString()

        bindingvan.btnAddVan.setOnClickListener {
            val intent = Intent(applicationContext, AddVanActivity::class.java)
            startActivity(intent)
        }
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingvan.bottomnavigation.setOnItemSelectedListener{
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
}
    override fun onResume() {
        super.onResume()
        callVanData()
    }

    fun callVanData(){
        vanticvanlist.clear();
        serv.retrieveVan()
            .enqueue(object : Callback<List<Van>> {
                override fun onResponse(call: Call<List<Van>>, response: Response<List<Van>>) {
                    response.body()?.forEach {
                        vanticvanlist.add(Van(it.vanid, it.registration_number,it.seats,it.driver))
                    }
                    //// Set Data to RecyclerRecyclerView
                    bindingvan.recyclerview.adapter = VanticAdapter(vanticvanlist,applicationContext)
                }
                override fun onFailure(call: Call<List<Van>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}