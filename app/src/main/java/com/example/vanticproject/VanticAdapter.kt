package com.example.vanticproject

import VanticAPI
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vanticproject.admin.EditTimeStationActivity
import com.example.vanticproject.admin.EditVanActivity
import com.example.vanticproject.admin.VanAdminActivity
import com.example.vanticproject.databinding.ShowcarItemLayoutBinding
import com.example.vanticproject.databinding.VanItemLayoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VanticAdapter(val vanticvanlist: ArrayList<Van>, val context: Context):
    RecyclerView.Adapter<VanticAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View,val binding: VanItemLayoutBinding) : RecyclerView.ViewHolder(itemView) {
        init {
            binding.btnEdit.setOnClickListener {
                val position = adapterPosition
                val item = vanticvanlist[position]
                val contextView: Context = itemView.context
                val vanid = vanticvanlist[position].vanid
                val intent = Intent(contextView, EditVanActivity::class.java)
                intent.putExtra("id",vanid)
                intent.putExtra("registration_number",item.registration_number)
                intent.putExtra("seat",item.seats)
                intent.putExtra("driver",item.driver)
                contextView.startActivity(intent)
            }

            binding.btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("การแจ้งเตือนการลบรายการ")
                builder.setMessage("คุณต้องการที่จะลบรายการนี้ ใช่หรือไม่")
                builder.setPositiveButton("ใช่") { dialog, which ->
                    val position = adapterPosition
                    val vanId = vanticvanlist[position].vanid
                    val serv : VanticAPI = Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(VanticAPI ::class.java)
                    serv.deletevan(vanId).enqueue(object : Callback<Van> {
                        override fun onResponse(call: Call<Van>, response: Response<Van>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context, "ลบรายการนี้สำเร็จ",
                                    Toast.LENGTH_LONG
                                ).show()
                                vanticvanlist.removeAt(position)
                                notifyDataSetChanged()
                            }
                        }
                        override fun onFailure(call: Call<Van>, t: Throwable) {
                            Toast.makeText(
                                context, "ลบไม่สำเร็จ กรุณาลองใหม่",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                }
                builder.setNegativeButton("ไม่") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VanItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var binding = holder.binding

        val color = if (vanticvanlist!![position].seats == 0) {
            binding.txtVanSeat.text = "ไม่มีที่นั่งว่าง"
            Color.parseColor("#F23A3A")
        } else {
            binding.txtVanSeat.text = "เหลือที่นั่งอยู่ : " + vanticvanlist!![position].seats.toString()
            Color.parseColor("#3AB4F2")
        }
        binding.txtVanSeat.setTextColor(color)
        binding.txtVanNumber.text = "รถคันที่ : " + vanticvanlist!![position].vanid.toString()
        binding.txtVanReg.text = "รถทะเบียน : " + vanticvanlist!![position].registration_number
        binding.txtVanDriver.text = "คนขับชื่อ : " + vanticvanlist!![position].driver
    }

    override fun getItemCount(): Int {
        return vanticvanlist!!.size
    }

}

