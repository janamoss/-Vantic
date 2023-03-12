package com.example.vanticproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vanticproject.databinding.ActivityEditProfileMainBinding
import com.example.vanticproject.databinding.ActivityProfileMainBinding

class EditProfileMainActivity : AppCompatActivity() {
    private lateinit var bindingProfile : ActivityEditProfileMainBinding
    lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingProfile = ActivityEditProfileMainBinding.inflate(layoutInflater)
        setContentView(bindingProfile.root)
        session = SessionManager(applicationContext)

        val fname: String? = session.pref.getString(SessionManager.KEY_FNAME, null)
        val lname: String? = session.pref.getString(SessionManager.KEY_LNAME, null)
        val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
        val phone: String? = session.pref.getString(SessionManager.KEY_PHONE, null)
        val password: String? = session.pref.getString(SessionManager.KEY_PHONE, null)

        bindingProfile.txtFName.setText(fname)
        bindingProfile.txtLName.setText(lname)
        bindingProfile.txtEmail.setText(email)
        bindingProfile.txtPhone.setText(phone)
        bindingProfile.txtPassword.setText(password)

        bindingProfile.btnSave.setOnClickListener {

        }
    }
}