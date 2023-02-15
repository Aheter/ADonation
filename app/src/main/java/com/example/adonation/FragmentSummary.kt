package com.example.adonation
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.adonation.databinding.FragmentPostsBinding
import com.example.adonation.databinding.FragmentSummaryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentSummary : Fragment() {
    private var _binding: FragmentSummaryBinding?=null
    private val binding get() = _binding!!
    private lateinit var auth:FirebaseAuth
    private lateinit var firebaseReference:FirebaseFirestore
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentSummaryBinding.inflate(inflater,container, false)
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()

        firebaseReference = FirebaseFirestore.getInstance()
        val userProfileName = binding.userProfileName

        if (uid!=null) {
            firebaseReference.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                    // Document found in the offline cache
                    userProfileName.text="Hello "+it?.getString("name")
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }else userProfileName.text="Hello "



        val btnAddDonation= binding.addDonation
        btnAddDonation.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    NewDonation::class.java
                )
            )

        }

        return binding.root
    }


}