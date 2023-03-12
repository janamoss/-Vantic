package com.example.vanticproject.date_time

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.vanticproject.R
import java.text.DateFormat
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener
{

    private lateinit var calendar: Calendar
    @SuppressLint("ResourceType")
    override fun onCreateDialog
                (savedInstanceState: Bundle?): Dialog
    {

// Initialize a calendar instance
        calendar = Calendar.getInstance()
// Get the system current date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
// Initialize a new date picker dialog and return it
        return DatePickerDialog(requireContext(), // Context
// Put 0 to system default theme or remove this parameter
        2, // Theme
        this, // DatePickerDialog.OnDateSetListener
        year, // Year
        month, // Month of year
        day // Day of month
        )
    }
    // When date set and press ok button in date picker dialog
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
// Display the selected date in text view
        var tv : androidx.appcompat.widget.AppCompatButton? = activity?.findViewById(R.id.btn_dateselect)
        var months = month+1
        tv!!.text = "$year:$months:$day"
    }
    override fun onCancel(dialog: DialogInterface) {
        Toast.makeText(activity,"Please select a date.", Toast.LENGTH_SHORT).show()
        super.onCancel(dialog)
    }
    // Custom method to format date
    private fun formatDate(year:Int, month:Int, day:Int):String{
        var calendar: Calendar = Calendar.getInstance();
// Create a Date variable/object with user chosen date
        calendar.set(year, month, day)
        val chosenDate = calendar.time
// Format the date picker selected date
        val df = DateFormat.getDateInstance(DateFormat.MEDIUM)
        return df.format(chosenDate)
    }
}
