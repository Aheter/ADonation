package com.example.adonation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        auth = Firebase.auth


        val registertext:TextView = findViewById(R.id.registerText)
        registertext.setOnClickListener{
            val intent = Intent(this,Registration::class.java)
            startActivity(intent)
        }
        val forgotPasswordText:TextView=findViewById(R.id.forgotPasswordText)
        forgotPasswordText.setOnClickListener{
            val intent = Intent(this,ForgotPassword::class.java)
            startActivity(intent)
        }

        val loginBtn:Button = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener {
            performLogin()
        }

    }


    private fun performLogin(){
        val email = findViewById<EditText>(R.id.emailLoginField)
        val password = findViewById<EditText>(R.id.passwordLoginField)
        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "Please,fill all fields.", Toast.LENGTH_LONG).show()
            return
        }
        val inputEmail=email.text.toString().trim()
        val inputPassword=password.text.toString().trim()
        auth.signInWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "Authentication success.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,HomeActivity::class.java)
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        intent.putExtra("id", user.uid)


                    }

                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Error occurred ${it.localizedMessage}.", Toast.LENGTH_LONG).show()
            }

    }

}