package com.example.vanticproject.admin

import VanticAPI
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.R
import com.example.vanticproject.Van
import com.example.vanticproject.databinding.ActivityAddVanBinding
import com.example.vanticproject.databinding.ActivityVanAdminBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddVanActivity : AppCompatActivity() {
    private lateinit var bindingaddvan: ActivityAddVanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingaddvan = ActivityAddVanBinding.inflate(layoutInflater)
        setContentView(bindingaddvan.root)

        bindingaddvan.btnDone3.setOnClickListener {
            insertvan()
        }
        bindingaddvan.btnReset3.setOnClickListener {
            val intent = Intent(applicationContext, VanAdminActivity::class.java)
            startActivity(intent)
        }
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingaddvan.bottomnavigation.setOnItemSelectedListener{
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

    private fun insertvan() {
        if(bindingaddvan.addTextName.text.toString().isEmpty() || bindingaddvan.addTextCar.text.toString().isEmpty() ) {
            Toast.makeText(applicationContext,"คุณกรอกข้อมูลไม่ครบ กรุณากรอกข้อมูลให้ครบถ้วน.",
                Toast.LENGTH_SHORT).show()
            return
        }
        val api:VanticAPI= Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VanticAPI::class.java)
        api.addvanlist(bindingaddvan.addTextCar.text.toString(),bindingaddvan.addTextName.text.toString())
            .enqueue(object : Callback<Van> {
            override fun onResponse(call: Call<Van>,
                                    response: retrofit2.Response<Van>)
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

            override fun onFailure(call: Call<Van>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure"+
                        t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}