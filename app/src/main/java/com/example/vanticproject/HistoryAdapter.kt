package com.example.vanticproject

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.TimeZone
import androidx.recyclerview.widget.RecyclerView
import com.example.vanticproject.databinding.BookhistoryItemLayoutBinding
import com.example.vanticproject.databinding.VanItemLayoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryAdapter(val historylist: ArrayList<Historybooking>, val context: Context):
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){
    val createClient = VanticAPI.create()
    inner class ViewHolder(itemView: View,val binding: BookhistoryItemLayoutBinding) : RecyclerView.ViewHolder(itemView) {
        init {
            binding.btnDetailmore.setOnClickListener {
                val position = adapterPosition
                val item = historylist[position]
                val contextView: Context = itemView.context
                val email = historylist[position].email
                createClient.bookinghistory(email).enqueue(object : Callback<List<Historybooking>> {
                    override fun onResponse(call: Call<List<Historybooking>>, response: Response<List<Historybooking>>) {
                        if (response.isSuccessful) {
                            val intent = Intent(contextView, BookingDetailActivity::class.java)
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

            binding.btnCanceled.setOnClickListener {
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
        val binding = BookhistoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val originalDateString = historylist!![position].date_time
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        originalFormat.timeZone = TimeZone.getTimeZone("UTC")

        val originalDate = originalFormat.parse(originalDateString)

        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getDefault()
        calendar.timeInMillis = originalDate.time

        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("th", "TH"))
        val year = calendar.get(Calendar.YEAR)

        val newDateString = "${dayOfMonth+1} $month $year"
        var binding = holder.binding
        binding.txtTime.text = "วันที่ "+newDateString
        binding.txtId.text = "รายการที่ ${historylist!![position].bookingid}"
        binding.statusCar.text = "${historylist!![position].namestatus}"
        binding.txtUser.text = "${historylist!![position].fname} ${historylist!![position].lname}"
        binding.txtTell.text = "เบอร์โทรศัพท์ ${historylist!![position].phone}"
        binding.txtTrip.text = "เที่ยวจาก ${historylist!![position].startname} ถึง ${historylist!![position].endname}"
        binding.txtBath.text = "฿ ${historylist!![position].price}"
        val color = if (binding.statusCar.text == "รถยังไม่ออกเดินทาง") {
            // กำหนดสีเป็นสีฟ้าสำหรับ A
            Color.parseColor("#3AB4F2")
        } else {
            Color.parseColor("#7C7979")
        }
        binding.statusCar.setTextColor(color)

        if (historylist!![position].namestatus == "รถออกเดินทางเรียบร้อยแล้ว") {
            binding.btnCanceled.visibility = View.GONE
        } else {
            binding.btnCanceled.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return historylist!!.size
    }
}