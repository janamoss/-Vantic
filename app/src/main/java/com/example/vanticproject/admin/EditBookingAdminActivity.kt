package com.example.vanticproject.admin

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.*
import com.example.vanticproject.databinding.ActivityBookingAdminBinding
import com.example.vanticproject.databinding.ActivityEditBookingAdminBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditBookingAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBookingAdminBinding
    lateinit var session: SessionManager
    private var statusList = mutableListOf<String>()
    var serv = VanticAPI.create()
    var statuss : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBookingAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(applicationContext)

        showDropdown()
        var data = intent
        var dates = data.getStringExtra("date").toString()
        var id = data.getIntExtra("id",0)
        var startname = data.getStringExtra("startname").toString()
        var endname = data.getStringExtra("endname").toString()
        var vanid = data.getIntExtra("vanid",0)
        var registration_number = data.getStringExtra("registration_number").toString()
        var time_start = data.getStringExtra("time_start").toString()
        var time_end = data.getStringExtra("time_end").toString()
        var seat = data.getIntExtra("seat",0)
        var price = data.getIntExtra("price",0)
        var statusname = data.getStringExtra("statusname").toString()

        val fname: String? = session.pref.getString(SessionManager.KEY_FNAME, null)
        val lname: String? = session.pref.getString(SessionManager.KEY_LNAME, null)
        val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
        val phone: String? = session.pref.getString(SessionManager.KEY_PHONE, null)
        val password: String? = session.pref.getString(SessionManager.KEY_PHONE, null)

        binding.txtId.text = "รายการที่ ${id}"
        binding.txtUser.text = "${fname} ${lname}"
        binding.driver.text = "เบอร์โทร ${phone}"
        binding.txtFrom.text = "เที่ยวรถจาก ${startname} ถึง ${endname}"
        binding.txtvanid.text = "รถตู้คันที่ ${vanid} เลขทะเบียน ${registration_number}"
        binding.txtdeparturetime.text = "เวลารถออก ${time_start}"
        binding.txtarrivetime.text = "เวลาถึงที่หมาย ${time_end}"
        binding.txtAmount.text = "จำนวนที่นั่งที่จอง ${seat}"
        binding.txtPrice.text = "ราคารวม ${price*seat}"


        binding.btnEdit.setOnClickListener {
            updatebook()
        }
        binding.btnDelete.setOnClickListener {
            deletebooking()
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

    fun getStatus(onSuccess: (List<Status>) -> Unit, onFailure: (Throwable) -> Unit) {
        serv.allstatus().enqueue(object : Callback<List<Status>> {
            override fun onResponse(call: Call<List<Status>>, response: Response<List<Status>>) {
                if (response.isSuccessful) {
                    val station = response.body()
                    if (station != null) {
                        onSuccess(station)
                    }
                } else {
                    onFailure(Throwable("Failed to fetch cities"))
                }
            }

            override fun onFailure(call: Call<List<Status>>, t: Throwable) {
                onFailure(t)
            }
        })

    }

    private fun addDataToList(status: List<Status>) {
        statusList.clear() // ล้างข้อมูลเดิมใน List
        statusList.addAll(status.map { it.namestatus })
    }


    private fun showDropdown () {
        getStatus(
            onSuccess = { status ->
                val stationNames = status.map { it.namestatus }
                val adapter = ArrayAdapter(this, R.layout.dropdown_seatvan, stationNames)
                binding.selectedFrom.setAdapter(adapter)
                binding.selectedFrom.setOnItemClickListener { parent, _, position, _ ->
                    statuss = parent.getItemIdAtPosition(position).toInt()+1
                }
            },
            onFailure = { error ->
                Log.e("AddTimeStationActivity", "Failed to fetch status", error)
            }
        )
    }
    private fun updatebook() {

        var data = intent
        var id = data.getIntExtra("id",0)
        if (statuss == null || id == null) {
            Toast.makeText(applicationContext,"คุณกรอกข้อมูลไม่ครบ กรุณากรอกข้อมูลให้ครบถ้วน.",
                Toast.LENGTH_SHORT).show()
            return
        }
        serv.updatebooking(
            statuss,
            id,
        ).enqueue(object : Callback<booking> {
            override fun onResponse(call: Call<booking>, response: Response<booking>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        applicationContext, "อัพเดตข้อมูลสำเร็จ",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<booking>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "อัพเดตข้อมูลไม่สำเร็จ",
                    Toast.LENGTH_LONG
                ).show()
            }
        })


    }

    private fun deletebooking() {
        var data = intent
        var id = data.getIntExtra("id",0)
        val mybuilder = AlertDialog.Builder(this)
        mybuilder.apply {
            setTitle("Warning Message")
            setMessage("Do you want to Delete this Student?")
            setNegativeButton("Yes") { dialog, which ->
                serv.deletebooking(id).enqueue(object : Callback<booking> {
                    override fun onResponse(call: Call<booking>, response: Response<booking>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                applicationContext, "ลบข้อมูลสำเร็จ",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    override fun onFailure(call: Call<booking>, t: Throwable) {
                        Toast.makeText(
                            applicationContext, "ลบข้อมูลไม่สำเร็จ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
                finish()
            }
            setPositiveButton("No") { dialog, which -> dialog.cancel()}
            show()
        }
    }

}