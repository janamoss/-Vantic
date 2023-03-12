package com.example.vanticproject

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.databinding.ActivityBookingDetailBinding
import com.example.vanticproject.databinding.ActivityHomeBinding
import java.text.SimpleDateFormat
import java.util.*

class BookingDetailActivity : AppCompatActivity() {
    private lateinit var bindingDetail : ActivityBookingDetailBinding
    lateinit var session: SessionManager
    val create = VanticAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDetail = ActivityBookingDetailBinding.inflate(layoutInflater)
        setContentView(bindingDetail.root)

        session = SessionManager(applicationContext)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingDetail.bottomnavigation.setOnItemSelectedListener{
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

        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        originalFormat.timeZone = TimeZone.getTimeZone("UTC")

        val originalDate = originalFormat.parse(dates)

        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getDefault()
        calendar.timeInMillis = originalDate.time

        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("th", "TH"))
        val year = calendar.get(Calendar.YEAR)

        val newDateString = "${dayOfMonth+1} $month $year"
        bindingDetail.txtid.text = "รายการที่ ${id}"
        bindingDetail.txtdate.text = "วันที่ ${newDateString}"
        bindingDetail.txtName.text = "${fname} ${lname}"
        bindingDetail.txtPhone.text = "เบอร์โทร ${phone}"
        bindingDetail.txtStartend.text = "เที่ยวรถจาก ${startname} ถึง ${endname}"
        bindingDetail.txtVanidres.text = "รถตู้คันที่ ${vanid} เลขทะเบียน ${registration_number}"
        bindingDetail.txtStartend.text = "เวลารถออก ${time_start}"
        bindingDetail.txtEndtime.text = "เวลาถึงที่หมาย ${time_end}"
        bindingDetail.txtSeats.text = "จำนวนที่นั่งที่จอง ${seat}"
        bindingDetail.txtPriceall.text = "ราคารวม ${price}"
        bindingDetail.txtStatus.text = statusname
        val color = if (bindingDetail.txtStatus.text == "รถยังไม่ออกเดินทาง") {
            // กำหนดสีเป็นสีฟ้าสำหรับ A
            Color.parseColor("#3AB4F2")
        } else {
            Color.parseColor("#7C7979")
        }
        bindingDetail.txtStatus.setTextColor(color)
    }
}