package com.example.vanticproject.admin

import User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.R
import com.example.vanticproject.SessionManager
import com.example.vanticproject.databinding.ActivityBookingAdminBinding
import com.example.vanticproject.databinding.ActivityEditaddminProfileBinding
import retrofit2.Call
import retrofit2.Callback

class EditaddminProfileActivity : AppCompatActivity() {
    private lateinit var bindingEditaddpro: ActivityEditaddminProfileBinding
    lateinit var session: SessionManager
    var serv = VanticAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editaddmin_profile)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { onBackPressed() }

        bindingEditaddpro.bottomnavigation.setOnItemSelectedListener{
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

        session = SessionManager(applicationContext)

        val fname: String? = session.pref.getString(SessionManager.KEY_FNAME, null)
        val lname: String? = session.pref.getString(SessionManager.KEY_LNAME, null)
        val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
        val phone: String? = session.pref.getString(SessionManager.KEY_PHONE, null)
        val password: String? = session.pref.getString(SessionManager.KEY_PASSWORD, null)

        bindingEditaddpro.txtFName.setText(fname)
        bindingEditaddpro.txtLName.setText(lname)
        bindingEditaddpro.txtEmail.setText(email)
        bindingEditaddpro.txtEmail.isEnabled = false
        bindingEditaddpro.txtPhone.setText(phone)
        bindingEditaddpro.txtPassword.setText(password)

        bindingEditaddpro.btnSave.setOnClickListener {
            updateuseradmin()
        }

    }

    private fun updateuseradmin() {


        if (bindingEditaddpro.txtEmail.text.toString().isEmpty() || bindingEditaddpro.txtPassword.text.toString().isEmpty()
            || bindingEditaddpro.txtFName.text.toString().isEmpty() || bindingEditaddpro.txtLName.text.toString().isEmpty()
            || bindingEditaddpro.txtPhone.text.toString().isEmpty() ) {
            Toast.makeText(applicationContext,"คุณกรอกข้อมูลไม่ครบ กรุณากรอกข้อมูลให้ครบถ้วน.",
                Toast.LENGTH_SHORT).show()
        }


        serv.updateuser(bindingEditaddpro.txtEmail.text.toString(),
            bindingEditaddpro.txtPassword.text.toString(),
            bindingEditaddpro.txtFName.text.toString(),
            bindingEditaddpro.txtLName.text.toString(),
            bindingEditaddpro.txtPhone.text.toString()
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