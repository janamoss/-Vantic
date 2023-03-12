package com.example.lab4u1

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.vanticproject.R
import java.util.*

class TimePickerFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, 2, this, hour, minute, true)
    }

    override fun onCancel(dialog: DialogInterface) {
        Toast.makeText(activity, "Please select a time.", Toast.LENGTH_LONG).show()
        super.onCancel(dialog)
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val minuteNew: String = if(minute <10) "0${minute.toString()}" else minute.toString()
        requireActivity().findViewById<TextView>(R.id.btn_time1).text = "$hour:$minute:00"

    }
}