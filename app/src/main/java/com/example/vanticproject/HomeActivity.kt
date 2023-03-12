package com.example.vanticproject

import User
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vanticproject.admin.HomeAdminActivity
import com.example.vanticproject.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var bindingHome : ActivityHomeBinding
    lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingHome = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bindingHome.root)
        session = SessionManager(applicationContext)

        val fname: String? = session.pref.getString(SessionManager.KEY_FNAME, null)
        val lname: String? = session.pref.getString(SessionManager.KEY_LNAME, null)
        val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
        val phone: String? = session.pref.getString(SessionManager.KEY_PHONE, null)
        val password: String? = session.pref.getString(SessionManager.KEY_PHONE, null)
        bindingHome.txtName.text = "Hello ${fname} ${lname}"

        bindingHome.btnBuy.setOnClickListener {
            val intent = Intent(applicationContext, BuyTicketVanActivity::class.java)
            intent.putExtra("fname",fname)
            intent.putExtra("lname",fname)
            intent.putExtra("email",email)
            intent.putExtra("phone",phone)
            startActivity(intent)
        }

        bindingHome.imgProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileMainActivity::class.java)
            startActivity(intent)
        }

        bindingHome.btnContact.setOnClickListener {
            val intent = Intent(applicationContext, ContactUsActivity::class.java)
            startActivity(intent)
        }

        bindingHome.btnRecomment.setOnClickListener {
            val intent = Intent(applicationContext, HowtoBuyActivity::class.java)
            startActivity(intent)
        }

        bindingHome.btnHistory.setOnClickListener {
            val intent = Intent(applicationContext, BookingHistoryActivity::class.java)
            startActivity(intent)
        }

        bindingHome.bottomnavigation.setOnItemSelectedListener{
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