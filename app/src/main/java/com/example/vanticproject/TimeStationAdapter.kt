package com.example.vanticproject

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vanticproject.admin.EditBookingAdminActivity
import com.example.vanticproject.admin.EditTimeStationActivity
import com.example.vanticproject.databinding.BookingItemLayoutBinding
import com.example.vanticproject.databinding.ShowcarItemLayoutBinding
import com.example.vanticproject.databinding.ShowtimestationItemLayoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TimeStationAdapter(val timeandstationlist: ArrayList<timestationshow>, val context: Context):
    RecyclerView.Adapter<TimeStationAdapter.ViewHolder>(){
    val createClient = VanticAPI.create()
    inner class ViewHolder(itemView: View, val binding: ShowcarItemLayoutBinding) : RecyclerView.ViewHolder(itemView) {
        init {
            binding.btnEdit.setOnClickListener {
                val position = adapterPosition
                val item = timeandstationlist[position]
                val contextView: Context = itemView.context
                val timetableid = timeandstationlist[position].timetableid
                val intent = Intent(contextView, EditTimeStationActivity::class.java)
                intent.putExtra("id",timetableid)
                intent.putExtra("startname",item.startname)
                intent.putExtra("endname",item.endname)
                intent.putExtra("vanid",item.vanid)
                intent.putExtra("registration_number",item.registration_number)
                intent.putExtra("time_start",item.time_start)
                intent.putExtra("time_end",item.time_end)
                intent.putExtra("date",item.date_time)
                intent.putExtra("seat",item.seats)
                intent.putExtra("price",item.price)
                intent.putExtra("statusname",item.namestatus)
                contextView.startActivity(intent)
            }

            binding.btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("การแจ้งเตือนการลบรายการ")
                builder.setMessage("คุณต้องการที่จะลบรายการนี้ ใช่หรือไม่")
                builder.setPositiveButton("ใช่") { dialog, which ->
                    val position = adapterPosition
                    val bookingid = timeandstationlist[position].timetableid
                    createClient.deletetimetable(bookingid).enqueue(object : Callback<timeandstation> {
                        override fun onResponse(call: Call<timeandstation>, response: Response<timeandstation>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context, "ลบรายการนี้สำเร็จ",
                                    Toast.LENGTH_LONG
                                ).show()
                                timeandstationlist.removeAt(position)
                                notifyDataSetChanged()
                            }
                        }
                        override fun onFailure(call: Call<timeandstation>, t: Throwable) {
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
        val binding = ShowcarItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var binding = holder.binding

        val color = if (timeandstationlist!![position].seats == 0) {
            binding.txtseat.text = "ไม่มีที่นั่งว่าง"
            Color.parseColor("#F23A3A")
        } else {
            binding.txtseat.text = "เหลือ : " + timeandstationlist!![position].seats.toString() + " ที่"
            Color.parseColor("#3AB4F2")
        }
        binding.txtseat.setTextColor(color)
        binding.txtxstart.text = "เที่ยวจาก " + timeandstationlist!![position].startname +" ถึง "+ timeandstationlist!![position].endname
        binding.txtcar.text = "รถตู้คันที่ : " + timeandstationlist!![position].vanid.toString()
        binding.txtintcar.text = "ป้ายทะเบียน : " + timeandstationlist!![position].registration_number
        binding.time1.text = timeandstationlist!![position].time_start
        binding.time2.text = timeandstationlist!![position].time_end
        binding.station1.text = timeandstationlist!![position].startname
        binding.station2.text = timeandstationlist!![position].endname

    }

    override fun getItemCount(): Int {
        return timeandstationlist!!.size
    }
}