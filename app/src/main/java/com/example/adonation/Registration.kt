package com.example.adonation

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.adonation.model.User
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

@Suppress("UNREACHABLE_CODE")
class Registration : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    val db= Firebase.firestore
    val mapper = ObjectMapper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        // Initialize Firebase Auth
        auth = Firebase.auth
        fstore=Firebase.firestore
        val logintext: TextView = findViewById(R.id.loginText)
        logintext.setOnClickListener{
            val intent = Intent(this,LogIn::class.java)
            startActivity(intent)
        }



        val autoCompleteGender=findViewById<AutoCompleteTextView>(R.id.autoCompleteGender)
        val textBirthdate=findViewById<TextView>(R.id.textBirthdate)

        //gender dropdown adapter
        val genders=resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_gender_item,genders)
        autoCompleteGender.setAdapter(arrayAdapter)

        //date picker dialog
        textBirthdate.setOnClickListener {
            val c = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                c.set(Calendar.YEAR,i)
                c.set(Calendar.MONTH,i2)
                c.set(Calendar.DAY_OF_MONTH,i3)
                val myFormat = "dd/MM/yyyy"
                textBirthdate.text= SimpleDateFormat(myFormat, Locale.US).format(c.getTime())
                val date = SimpleDateFormat(myFormat, Locale.US).format(c.getTime())
            }
            val dtd = DatePickerDialog(this,dateSetListener,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(
                Calendar.DAY_OF_MONTH)).show()
        }


        val registerBtn=findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener{
            performSignUp()
        }

    }

    private fun performSignUp(){
        val email = findViewById<EditText>(R.id.textEmailField)
        val password = findViewById<EditText>(R.id.textPasswordField)
        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "Please, fill in all fields.", Toast.LENGTH_LONG).show()
            return
        }
        val inputEmail=email.text.toString().trim()
        val inputPassword=password.text.toString().trim()



        val textPersonName = findViewById<EditText>(R.id.textPersonName)
        val textBloodType = findViewById<EditText>(R.id.textBloodType)
        val autoCompleteGender=findViewById<AutoCompleteTextView>(R.id.autoCompleteGender)
        val textBirthdate=findViewById<TextView>(R.id.textBirthdate)
        if (textPersonName.text.isEmpty() || textBloodType.text.isEmpty()|| textBirthdate.text.isEmpty()|| autoCompleteGender.text.isEmpty()) {
            Toast.makeText(this, "Please, fill in all fields.", Toast.LENGTH_LONG).show()
            return
        }

        val name=textPersonName.text.toString().trim()
        val bloodType=textBloodType.text.toString().trim()
        val gender= autoCompleteGender.text.toString().trim()
        val birthday=textBirthdate.text.toString().trim()




        auth.createUserWithEmailAndPassword(inputEmail,inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this,LogIn::class.java)
                    val currentUser = FirebaseAuth.getInstance().currentUser

                    currentUser?.let { val userID = currentUser.uid
                        val documentReference = db.collection("users").document(userID)

                        // Create a new user with a first, middle, and last name
                        val user = hashMapOf(
                            "name" to name,
                            "birthday" to birthday,
                            "gender" to gender,
                            "bloodType" to bloodType
                        )
                        //added user detailes
                        val userO=User(userID,inputEmail,inputPassword,name,gender,bloodType,birthday)
                        intent.putExtra("id", userID)


                        documentReference.set(user).addOnSuccessListener { documentReference ->
                            Toast.makeText(baseContext, "User added successfully.",Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener { e ->
                                Toast.makeText(baseContext, "Error adding document.",Toast.LENGTH_SHORT).show()
                            }
                    }
                    startActivity(intent)
                }
                else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_LONG).show()
                }

            }
            .addOnFailureListener{
                Toast.makeText(this, "Error occurred ${it.localizedMessage}.", Toast.LENGTH_LONG).show()
            }


    }
}