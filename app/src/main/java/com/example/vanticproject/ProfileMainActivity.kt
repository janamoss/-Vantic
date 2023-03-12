package com.example.vanticproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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