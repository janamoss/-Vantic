package com.example.vanticproject

import User
import VanticAPI
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.vanticproject.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var bindingRegister : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingRegister = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bindingRegister.root)


        bindingRegister.btnSubmitRegister.setOnClickListener{
            insertUser()
        }
    }

    private fun insertUser() {
        var password = ""
        var userid = 1
        if(userid == 1 && bindingRegister.edtEmail.text.toString().isEmpty()
            || bindingRegister.edtPassword.text.toString().isEmpty()
            || bindingRegister.edtFName.text.toString().isEmpty()
            || bindingRegister.edtLName.text.toString().isEmpty()
            || bindingRegister.edtNumber.text.toString().isEmpty()
            || bindingRegister.edtPasswordA.text.toString().isEmpty()) {
            Toast.makeText(applicationContext,"คุณกรอกข้อมูลไม่ครบ กรุณากรอกข้อมูลให้ครบถ้วน.",
                Toast.LENGTH_SHORT).show()
        }
        if(bindingRegister.edtPassword.text.toString().equals(bindingRegister.edtPasswordA.text.toString())) {
            password = bindingRegister.edtPassword.text.toString()
        } else {
            Toast.makeText(applicationContext,"รหัสผ่านไม่ตรงกัน กรุณากรอกใหม่อีกครั้ง.",
                Toast.LENGTH_SHORT).show()
        }
        val api:VanticAPI= Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VanticAPI::class.java)
        api.register(
            bindingRegister.edtEmail.text.toString(),
            password,
            bindingRegister.edtFName.text.toString(),
            bindingRegister.edtLName.text.toString(),
            bindingRegister.edtNumber.text.toString(),
            userid
        ).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>,
                                    response: retrofit2.Response<User>)
            {
                if (response.isSuccessful()){
                    Toast.makeText(applicationContext,"ลงทะเบียนสำเร็จ",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(applicationContext,"ลงทะเบียนไม่สำเร็จ กรุณาลองใหม่อีกครั้ง",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure"+
                        t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}