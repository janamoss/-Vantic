package com.example.vanticproject.admin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vanticproject.BookingDetailActivity
import com.example.vanticproject.Historybooking
import com.example.vanticproject.Van
import com.example.vanticproject.booking
import com.example.vanticproject.databinding.BookingItemLayoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BookingAdapter(val historylist: ArrayList<Historybooking>, val context: Context):
    RecyclerView.Adapter<BookingAdapter.ViewHolder>(){
    val createClient = VanticAPI.create()
    inner class ViewHolder(itemView: View, val binding: BookingItemLayoutBinding) : RecyclerView.ViewHolder(itemView) {
        init {
            binding.btnEdit.setOnClickListener {
                val position = adapterPosition
                val item = historylist[position]
                val contextView: Context = itemView.context
                val email = historylist[position].email
                createClient.bookinghistory(email).enqueue(object : Callback<List<Historybooking>> {
                    override fun onResponse(call: Call<List<Historybooking>>, response: Response<List<Historybooking>>) {
                        if (response.isSuccessful) {
                            val intent = Intent(contextView, EditBookingAdminActivity::class.java)
                            intent.putExtra("date",item.date_time)
                            intent.putExtra("id",item.bookingid)
                            intent.putExtra("startname",item.startname)
                            intent.putExtra("endname",item.endname)
                            intent.putExtra("vanid",item.vanid)
                            intent.putExtra("registration_number",item.registration_number)
                            intent.putExtra("time_start",item.time_start)
                            intent.putExtra("time_end",item.time_end)
                            intent.putExtra("seat",item.seat_number)
                            intent.putExtra("price",item.price)
                            intent.putExtra("statusname",item.namestatus)
                            contextView.startActivity(intent)
                        }
                    }
                    override fun onFailure(call: Call<List<Historybooking>>, t: Throwable) {
                        Toast.makeText(
                            context, "Failure",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }

            binding.btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("การแจ้งเตือนการลบรายการ")
                builder.setMessage("คุณต้องการที่จะลบรายการนี้ ใช่หรือไม่")
                builder.setPositiveButton("ใช่") { dialog, which ->
                    val position = adapterPosition
                    val bookingid = historylist[position].bookingid
                    createClient.deletebooking(bookingid).enqueue(object : Callback<booking> {
                        override fun onResponse(call: Call<booking>, response: Response<booking>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context, "ลบรายการนี้สำเร็จ",
                                    Toast.LENGTH_LONG
                                ).show()
                                historylist.removeAt(position)
                                notifyDataSetChanged()
                            }
                        }
                        override fun onFailure(call: Call<booking>, t: Throwable) {
                            Toast.makeText(
                                context, "ลบไม่สำเร็จกรุณาลองใหม่",
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
        val binding = BookingItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var binding = holder.binding
        binding.txtid.text = "รายการที่ ${historylist!![position].bookingid}"
        binding.statuscar.text = "${historylist!![position].namestatus}"
        binding.txtuser.text = "${historylist!![position].fname} ${historylist!![position].lname}"
        binding.txtphone.text = "เบอร์โทรศัพท์ ${historylist!![position].phone}"
        binding.txtstart.text = "เที่ยวจาก ${historylist!![position].startname} ถึง ${historylist!![position].endname}"
        binding.txtbath.text = "฿ ${historylist!![position].price*historylist!![position].seat_number}"
        val color = if (binding.statuscar.text == "รถยังไม่ออกเดินทาง") {
            // กำหนดสีเป็นสีฟ้าสำหรับ A
            Color.parseColor("#3AB4F2")
        } else {
            Color.parseColor("#7C7979")
        }
        binding.statuscar.setTextColor(color)
    }

    override fun getItemCount(): Int {
        return historylist!!.size
    }
}