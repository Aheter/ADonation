package com.example.adonation

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adonation.databinding.ActivityForgotPasswordBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import java.util.Objects

class ForgotPassword : AppCompatActivity() {
    private var binding: ActivityForgotPasswordBinding? = null
    private var usersAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersAuth = FirebaseAuth.getInstance()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)
        binding!!.submitButton.setOnClickListener { v ->
            val email =
                Objects.requireNonNull(binding!!.emailField.text).toString()
                    .trim { it <= ' ' }
            if (email.isEmpty()) {
                binding!!.emailField.error = "You mast enter an email address"
                Toast.makeText(applicationContext, "Enter correct Email", Toast.LENGTH_SHORT)
                    .show()
            } else if (!isEmailValid(email)) {
                binding!!.emailField.error = "Invalid email address"
                Toast.makeText(applicationContext, "Enter correct Email", Toast.LENGTH_SHORT)
                    .show()
            } else {
                usersAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener(
                        this@ForgotPassword
                    ) { task: Task<Void?> ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Reset password request was sent to your Email",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                Objects.requireNonNull(task.exception)
                                    .toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
        clear()
    }

    private fun clear() {
        binding!!.emailField.setText("")
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}