package com.example.vanticproject.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.ProfileMainActivity
import com.example.vanticproject.R
import com.example.vanticproject.SessionManager
import com.example.vanticproject.databinding.ActivityHomeAdminBinding

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var bindingHomeA : ActivityHomeAdminBinding
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingHomeA = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(bindingHomeA.root)
        session = SessionManager(applicationContext)

        val fname: String? = session.pref.getString(SessionManager.KEY_FNAME, null)
        val lname: String? = session.pref.getString(SessionManager.KEY_LNAME, null)
        val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
        val phone: String? = session.pref.getString(SessionManager.KEY_PHONE, null)
        val password: String? = session.pref.getString(SessionManager.KEY_PHONE, null)

        bindingHomeA.nameaccount.text = "${fname} ${lname}."

        bindingHomeA.icon.setOnClickListener {
            val intent = Intent(applicationContext, ProfileMainActivity::class.java)
            startActivity(intent)
        }

        bindingHomeA.allvan.setOnClickListener {
            val intent = Intent(applicationContext, VanAdminActivity::class.java)
            startActivity(intent)
        }
        bindingHomeA.Timetable.setOnClickListener {
            val intent = Intent(applicationContext, TimeStationAdminActivity::class.java)
            startActivity(intent)
        }

        bindingHomeA.allbooking.setOnClickListener {
            val intent = Intent(applicationContext, BookingAdminActivity::class.java)
            startActivity(intent)
        }





        bindingHomeA.bottomnavigation.setOnItemSelectedListener{
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
}