package com.example.vanticproject.admin

import VanticAPI
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
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
    var seat: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingaddvan = ActivityAddVanBinding.inflate(layoutInflater)
        setContentView(bindingaddvan.root)
        showDropdown()

        bindingaddvan.btnDone3.setOnClickListener {
            insertvan()
        }
        bindingaddvan.btnReset3.setOnClickListener {
            val intent = Intent(applicationContext, VanAdminActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showDropdown () {
        bindingaddvan.dropdownAdd1.setText("กรุณาเลือกที่นั่ง")
        val sub = resources.getStringArray (R.array.VanSeat_array)
        val arrayAdapter = ArrayAdapter(this , R.layout.dropdown_seatvan , sub)
        bindingaddvan.dropdownAdd1.setAdapter(arrayAdapter)
        bindingaddvan.dropdownAdd1.setOnItemClickListener { parent, _, position, _ ->
            seat = parent.getItemAtPosition(position) as String
        }
    }
    private fun insertvan() {
        if(seat == null || bindingaddvan.addTextName.text.toString().isEmpty() || bindingaddvan.addTextCar.text.toString().isEmpty() ) {
            Toast.makeText(applicationContext,"คุณกรอกข้อมูลไม่ครบ กรุณากรอกข้อมูลให้ครบถ้วน.",
                Toast.LENGTH_SHORT).show()
        }
        val api:VanticAPI= Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VanticAPI::class.java)
        api.addvanlist(bindingaddvan.addTextCar.text.toString(),seat.toInt(),bindingaddvan.addTextName.text.toString())
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