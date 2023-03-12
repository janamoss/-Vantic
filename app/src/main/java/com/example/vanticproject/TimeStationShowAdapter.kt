package com.example.vanticproject

import android.R
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vanticproject.databinding.ShowtimestationItemLayoutBinding


class TimeStationShowAdapter(val timeandstationlist: ArrayList<timestationshow>,val seat : Int,val fname : String,val lname : String,val email : String,val phone : String, val context: Context):
    RecyclerView.Adapter<TimeStationShowAdapter.ViewHolder>() {

    inner class ViewHolder(view : View,val binding: ShowtimestationItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            val timeStation = timeandstationlist[position]
            val intent = Intent(context, BookingReserveActivity::class.java)
            intent.putExtra("user_seat", seat)
            intent.putExtra("time_station_vanid", timeStation.vanid.toString())
            intent.putExtra("time_station_seats", timeStation.seats)
            intent.putExtra("time_station_registration_number", timeStation.registration_number)
            intent.putExtra("time_station_startname", timeStation.startname)
            intent.putExtra("time_station_endname", timeStation.endname)
            intent.putExtra("time_station_id", timeStation.timetableid.toString())
            intent.putExtra("user_fname", fname)
            intent.putExtra("user_lname", lname)
            intent.putExtra("user_email", email)
            intent.putExtra("user_phone", phone)
            intent.putExtra("time_start",timeStation.time_start)
            intent.putExtra("time_end",timeStation.time_end)
            intent.putExtra("time_station_price", timeStation.price)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ShowtimestationItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var binding = holder.binding
        binding.txtstart.text = "เที่ยวจาก " + timeandstationlist!![position].startname +" ถึง "+ timeandstationlist!![position].endname

        val color = if (timeandstationlist!![position].seats == 0) {
            binding.txtseat.text = "ไม่มีที่นั่งว่าง"
            Color.parseColor("#F23A3A")
        } else {
            binding.txtseat.text = "เหลือ : " + timeandstationlist!![position].seats.toString() + " ที่นั่ง"
            Color.parseColor("#3AB4F2")
        }
        binding.txtseat.setTextColor(color)

        binding.txtbath.text = timeandstationlist!![position].price.toString() + " บาท."
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