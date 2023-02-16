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
import com.example.adonation.databinding.FragmentAddDonationBinding
import com.example.adonation.databinding.FragmentProfileBinding
import com.example.adonation.model.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NewDonation: AppCompatActivity() {
    private var _binding: FragmentAddDonationBinding?=null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentAddDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //setContentView(R.layout.fragment_add_donation)


        //val addBtn= findViewById<Button>(R.id.add)
        val addBtn= binding.add
        val date= findViewById<AutoCompleteTextView>(R.id.newrecord_date)
        val liters=findViewById<EditText>(R.id.liters)
        val Dtype=findViewById<AutoCompleteTextView>(R.id.type)
        val location=findViewById<AutoCompleteTextView>(R.id.locationInput)
        val type=resources.getStringArray(R.array.type)
        val arrayAdapterT = ArrayAdapter(this,R.layout.dropdown_type_item,type)
        Dtype.setAdapter(arrayAdapterT)
        val loc=resources.getStringArray(R.array.locations)
        val arrayAdapterL = ArrayAdapter(this,R.layout.dropdown_location_item,loc)
        location.setAdapter(arrayAdapterL)

        val cal = Calendar.getInstance()
        updateDateInView(date, cal)



        val dateSetListener =DatePickerDialog.OnDateSetListener{ view, year, monthOfYear, dayOfMonth ->
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
            addDonation()
            finish()
        }

    }

    private fun updateDateInView(date: AutoCompleteTextView, cal: Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat)
        date!!.setText(sdf.format(cal.getTime()))
    }



    private fun addDonation(){
    auth = FirebaseAuth.getInstance()
    val db= Firebase.firestore
    val userID = auth.currentUser?.uid.toString()
    val documentReference = db.collection("donations").document(userID)
    val dateField= findViewById<AutoCompleteTextView>(R.id.newrecord_date)
    if (binding.liters.text.isEmpty() || dateField.text.isEmpty()|| binding.type.text.isEmpty()|| binding.locationInput.text.isEmpty()) {
        Toast.makeText(this, "Please, fill in all fields.", Toast.LENGTH_LONG).show()
        return
    }

    val date=dateField.text.toString().trim()
    val liters=binding.liters.text.toString().trim()
    val btype=binding.type.text.toString().trim()
    val location=binding.locationInput.text.toString().trim()

    // Create a new donation
    val donation = hashMapOf(
        "location" to location,
        "date" to date,
        "btype" to btype,
        "liters" to liters
    )



    documentReference.set(donation).addOnSuccessListener { documentReference ->
        Toast.makeText(baseContext, "Donation added successfully.",Toast.LENGTH_SHORT).show()
    }
        .addOnFailureListener { e ->
            Toast.makeText(baseContext, "Error adding document.",Toast.LENGTH_SHORT).show()
        }

}

}

