package com.example.vanticproject

import User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.databinding.ActivityEditProfileMainBinding
import com.example.vanticproject.databinding.ActivityProfileMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileMainActivity : AppCompatActivity() {
    private lateinit var bindingProfile : ActivityEditProfileMainBinding
    lateinit var session: SessionManager
    var serv = VanticAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingProfile = ActivityEditProfileMainBinding.inflate(layoutInflater)
        setContentView(bindingProfile.root)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingProfile.bottomnavigation.setOnItemSelectedListener{
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

        session = SessionManager(applicationContext)

        val fname: String? = session.pref.getString(SessionManager.KEY_FNAME, null)
        val lname: String? = session.pref.getString(SessionManager.KEY_LNAME, null)
        val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
        val phone: String? = session.pref.getString(SessionManager.KEY_PHONE, null)
        val password: String? = session.pref.getString(SessionManager.KEY_PASSWORD, null)

        bindingProfile.txtFName.setText(fname)
        bindingProfile.txtLName.setText(lname)
        bindingProfile.txtEmail.setText(email)
        bindingProfile.txtEmail.isEnabled = false
        bindingProfile.txtPhone.setText(phone)
        bindingProfile.txtPassword.setText(password)

        bindingProfile.btnSave.setOnClickListener {
            updateuser()
        }
    }

    private fun updateuser() {
        serv.updateuser(bindingProfile.txtEmail.text.toString(),
            bindingProfile.txtPassword.text.toString(),
            bindingProfile.txtFName.text.toString(),
            bindingProfile.txtLName.text.toString(),
            bindingProfile.txtPhone.text.toString()
            ).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>,
                                    response: retrofit2.Response<User>)
            {
                if (response.isSuccessful()){
                    Toast.makeText(applicationContext,"อัพเดตข้อมูลสำเร็จ",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext,"อัพเดตข้อมูลไม่สำเร็จ"+
                        t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}