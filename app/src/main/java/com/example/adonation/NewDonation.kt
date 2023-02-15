package com.example.adonation

import android.os.Bundle
import android.app.DatePickerDialog
import android.os.Build
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NewDonation: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_donation)
        val addBtn= findViewById<Button>(R.id.add)
        val date= findViewById<AutoCompleteTextView>(R.id.newrecord_date)
        val liters=findViewById<EditText>(R.id.liters)
        val Dtype=findViewById<AutoCompleteTextView>(R.id.type)
        val type=resources.getStringArray(R.array.type)
        val arrayAdapterT = ArrayAdapter(this,R.layout.dropdown_type_item,type)
        Dtype.setAdapter(arrayAdapterT)
        val location=findViewById<TextInputLayout>(R.id.location)
        val loc=resources.getStringArray(R.array.locations)
        val arrayAdapterL = ArrayAdapter(this,R.layout.dropdown_location_item,loc)
        location.setAdapter(arrayAdapterL)

        val cal = Calendar.getInstance()
        updateDateInView(date, cal)
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(date, cal)
            }
        date.setOnClickListener(object :View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@NewDonation,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        })

        addBtn.setOnClickListener{
            finish()
        }

    }

    private fun updateDateInView(date: AutoCompleteTextView, cal: Calendar) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat)
        date!!.setText(sdf.format(cal.getTime()))
    }



}

