package com.example.vanticproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.databinding.ActivityHomeBinding
import com.example.vanticproject.databinding.ActivityProfileMainBinding

class ProfileMainActivity : AppCompatActivity() {
    private lateinit var bindingPro : ActivityProfileMainBinding
    lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingPro = ActivityProfileMainBinding.inflate(layoutInflater)
        setContentView(bindingPro.root)
        session = SessionManager(applicationContext)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingPro.bottomnavigation.setOnItemSelectedListener{
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

        val fname: String? = session.pref.getString(SessionManager.KEY_FNAME, null)
        val lname: String? = session.pref.getString(SessionManager.KEY_LNAME, null)
        val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
        val phone: String? = session.pref.getString(SessionManager.KEY_PHONE, null)
        val password: String? = session.pref.getString(SessionManager.KEY_PHONE, null)
        val hiddenPassword = password?.toCharArray()?.map { '*' }?.joinToString("")

        bindingPro.txtEmail.text = email
        bindingPro.txtName.text = fname+" "+lname
        bindingPro.txtPhone.text = phone
        bindingPro.txtPassword.text = hiddenPassword

        bindingPro.btnLogout.setOnClickListener {
            val edit = session.edior
            edit.clear()
            edit.commit()

            var i = Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        bindingPro.btnEditPf.setOnClickListener {
            val intent = Intent(applicationContext,EditProfileMainActivity::class.java)
            startActivity(intent)
        }
    }
}