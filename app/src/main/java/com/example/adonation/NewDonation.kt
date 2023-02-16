package com.example.adonation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.adonation.databinding.FragmentAddDonationBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*




class NewDonation: AppCompatActivity() {
    private var _binding: FragmentAddDonationBinding?=null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    val db= Firebase.firestore

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

    fun getRandomString(length: Int=9) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun addDonation(){
        auth = FirebaseAuth.getInstance()
        val userID = auth.currentUser?.uid.toString()
        val donationid=getRandomString()
        val documentReference = db.collection("donations").document(userID)
            .collection("donations").document(donationid)

        val dateField= findViewById<AutoCompleteTextView>(R.id.newrecord_date)
        if (binding.liters.text.isEmpty() || dateField.text.isEmpty()|| binding.type.text.isEmpty()|| binding.locationInput.text.isEmpty()) {
            Toast.makeText(this, "Please, fill in all fields.", Toast.LENGTH_LONG).show()
            return
        }

        val date=dateField.text.toString().trim()
        val liters=binding.liters.text.toString().toDouble()
        val type=binding.type.text.toString().trim()
        val location=binding.locationInput.text.toString().trim()

        // Create a new donation
        val donation = hashMapOf(
            "location" to location,
            "date" to date,
            "type" to type,
            "liters" to liters
        )
    documentReference.set(donation).addOnSuccessListener { documentReference ->
        Toast.makeText(baseContext, "Donation added successfully.",Toast.LENGTH_SHORT).show()
        updateSummary(userID,date,liters,type)
    }
        .addOnFailureListener { e ->
            Toast.makeText(baseContext, "Error adding document.",Toast.LENGTH_SHORT).show()
        }

}
    private fun updateSummary(uid:String,date:String,liters:Double,type:String){
        val donatedBaseValue=0
        val documentUserRef = db.collection("users").document(uid)
        val documentSummaryRef = db.collection("users").document(uid).collection("summary").document(uid)

        documentSummaryRef.get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
            val document = task.result

            if (task.isSuccessful) {
                if (document.exists()) {
                    var num:Double=document.get("donated").toString().toDouble()+liters
                    documentSummaryRef.update("donated",num)
                    documentSummaryRef.update("date",date)
                    documentSummaryRef.update("type",type)

                    Toast.makeText(baseContext, "Document exists!",Toast.LENGTH_SHORT).show()
                } else {

                    val summary = hashMapOf(
                        "donated" to liters,
                        "type" to type,
                        "date" to date
                    )
                    documentSummaryRef.set(summary).addOnSuccessListener { documentSummaryRef ->
                        Toast.makeText(baseContext, "Summary added successfully.",Toast.LENGTH_SHORT).show()
                        // updateSummary(userID)
                    }
                        .addOnFailureListener { e ->
                            Toast.makeText(baseContext, "Error adding summary.",Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(baseContext, "Failed with:"+task.exception,Toast.LENGTH_SHORT).show()
            }
        }


    }

}

