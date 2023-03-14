package com.example.vanticproject.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.*
import com.example.vanticproject.databinding.ActivityAddminProfileBinding
import com.example.vanticproject.databinding.ActivityBookingDetailBinding
import com.example.vanticproject.databinding.ActivityProfileMainBinding

class AddminProfileActivity : AppCompatActivity() {
    private lateinit var bindingAddminProfile : ActivityAddminProfileBinding
    lateinit var session: SessionManager
    var serv = VanticAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmin_profile)

        session = SessionManager(applicationContext)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingAddminProfile.bottomnavigation.setOnItemSelectedListener{
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

        val fname: String? = session.pref.getString(SessionManager.KEY_FNAME, null)
        val lname: String? = session.pref.getString(SessionManager.KEY_LNAME, null)
        val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
        val phone: String? = session.pref.getString(SessionManager.KEY_PHONE, null)
        val password: String? = session.pref.getString(SessionManager.KEY_PASSWORD, null)
        val hiddenPassword = password?.toCharArray()?.map { '*' }?.joinToString("")

        bindingAddminProfile.txtEmail.text = email
        bindingAddminProfile.txtName.text = fname+" "+lname
        bindingAddminProfile.txtPhone.text = phone
        bindingAddminProfile.txtPassword.text = hiddenPassword

        bindingAddminProfile.btnLogout.setOnClickListener {
            val edit = session.edior
            edit.clear()
            edit.commit()

            var i = Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        bindingAddminProfile.btnEditPf.setOnClickListener {
            val intent = Intent(applicationContext,EditProfileMainActivity::class.java)
            startActivity(intent)
        }
    }
}