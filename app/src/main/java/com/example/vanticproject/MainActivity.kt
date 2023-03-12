package com.example.vanticproject

import LoginResponse
import User
import VanticAPI
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vanticproject.admin.HomeAdminActivity
import com.example.vanticproject.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    var userlist  = arrayListOf<User>()
    lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root )

        binding.btnRegister.setOnClickListener {
            val intent = Intent(applicationContext,RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            LoginUser()
        }
        session = SessionManager(applicationContext)
        if (session.isLoggedIn()) {
            val homeIntent = session.getHomeActivityIntent(this)
            startActivity(homeIntent)
            finish()
        }
        if (!session.pref.getString(SessionManager. KEY_EMAIL, null).isNullOrEmpty()){
            val email: String? = session.pref.getString(SessionManager. KEY_EMAIL, null)
            binding.edtEmail.setText(email)
        }
    }
    private fun LoginUser() {
        var email = binding.edtEmail.text.toString()
        var password = binding.edtPassword.text.toString()
        if (binding.edtEmail.text.toString().isEmpty()
            || binding.edtPassword.text.toString().isEmpty()) {
            Toast.makeText(applicationContext,"กรุณากรอกข้อมูลก่อนเข้าสู่ระบบ",
                Toast.LENGTH_SHORT).show()
        }
        val api:VanticAPI= Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VanticAPI::class.java)
        api.userLogin(email,password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse> , response: retrofit2.Response<LoginResponse> )
            {
                val usertype = response.body()?.usertypeid.toString().toInt()
                if (usertype == 1) {
                    val success = response.body()?.success.toString().toInt()
                    if(success == 0){
                        Toast.makeText(applicationContext, "อีเมลหรือรหัสผ่านของคุณไม่ถูกต้อง กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_LONG).show()
                    }else{
                        val email = response.body()?.email.toString()
                        val fname = response.body()?.fname.toString()
                        val lname = response.body()?.lname.toString()
                        val phone = response.body()?.phone.toString()
                        val password = response.body()?.password.toString()
                        session.createLoginSession(email, fname, lname,phone,password,usertype)
                        var i = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(i)
                        finish() }
                } else {
                    val success = response.body()?.success.toString().toInt()
                    if(success == 0){
                        Toast.makeText(applicationContext, "อีเมลหรือรหัสผ่านของคุณไม่ถูกต้อง กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_LONG).show()
                    }else{
                        val email = response.body()?.email.toString()
                        val fname = response.body()?.fname.toString()
                        val lname = response.body()?.lname.toString()
                        val phone = response.body()?.phone.toString()
                        val password = response.body()?.password.toString()
                        session.createLoginSession(email, fname, lname,phone,password,usertype)
                        var i = Intent(applicationContext, HomeAdminActivity::class.java)
                        startActivity(i)
                        finish() }
                }

            }

            override fun onFailure(call: Call<LoginResponse> , t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure"+
                        t.message, Toast.LENGTH_LONG).show()
                println(t.message)
            }
        })
    }
}
