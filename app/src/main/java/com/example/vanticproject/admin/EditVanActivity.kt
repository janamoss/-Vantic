package com.example.vanticproject.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.R
import com.example.vanticproject.SessionManager
import com.example.vanticproject.Van
import com.example.vanticproject.databinding.ActivityEditVanBinding
import com.example.vanticproject.timestationshow
import retrofit2.Call
import retrofit2.Callback

class EditVanActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditVanBinding
    lateinit var session: SessionManager
    var serv = VanticAPI.create()
    var seat = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditVanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(applicationContext)
        showDropdown()
        var data = intent
        var seats = data.getIntExtra("seat",0)
        var id = data.getIntExtra("id",0)
        var registration_number = data.getStringExtra("registration_number").toString()
        var driver = data.getStringExtra("driver").toString()

        binding.txtVan.text = "รถตู้คันที่ $id"
        binding.txtVannum.text = "เลขทะเบียน $registration_number"
        binding.editTextName.setText(driver)

        binding.btnDone1.setOnClickListener {
            updatevan()
        }

        binding.btnReset1.setOnClickListener {
            val intent = Intent(applicationContext, VanAdminActivity::class.java)
            startActivity(intent)
        }
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }


        binding.bottomnavigation.setOnItemSelectedListener{
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

    private fun showDropdown () {
        var data = intent
        var seats = data.getIntExtra("seat",0)
        val sub = resources.getStringArray (R.array.VanSeat_array)
        val arrayAdapter = ArrayAdapter(this , R.layout.dropdown_seatvan , sub)
        binding.autoCompleteTextView4.setText(seats.toString())
        binding.autoCompleteTextView4.setAdapter(arrayAdapter)
        binding.autoCompleteTextView4.setOnItemClickListener { parent, _, position, _ ->
            seat = parent.getItemAtPosition(position) as String
        }
    }

    private fun updatevan() {

        if (binding.txtVan.text.toString().isEmpty() || binding.txtVannum.text.toString().isEmpty()
            || binding.editTextName.text.toString().isEmpty()) {
            Toast.makeText(applicationContext,"คุณกรอกข้อมูลไม่ครบ กรุณากรอกข้อมูลให้ครบถ้วน.",
                Toast.LENGTH_SHORT).show()
        }

        var data = intent
        var id = data.getIntExtra("id",0)
        var registration_number = data.getStringExtra("registration_number").toString()
        var driver = data.getStringExtra("driver").toString()
        serv.updatevan(
            id,
            registration_number,
            seat.toInt(),
            driver
        ).enqueue(object : Callback<Van> {
            override fun onResponse(call: Call<Van>,
                                    response: retrofit2.Response<Van>)
            {
                if (response.isSuccessful()){
                    Toast.makeText(applicationContext,"อัพเดตข้อมูลสำเร็จ",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Van>, t: Throwable) {
                Toast.makeText(applicationContext,"อัพเดตข้อมูลไม่สำเร็จ"+
                        t.message, Toast.LENGTH_LONG).show()
            }
        })


    }
}