package com.example.adonation
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.adonation.databinding.FragmentPostsBinding
import com.example.adonation.databinding.FragmentSummaryBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentSummary : Fragment() {
    private var _binding: FragmentSummaryBinding?=null
    private val binding get() = _binding!!
    private lateinit var auth:FirebaseAuth
    private lateinit var firebaseReference:FirebaseFirestore
    val db= Firebase.firestore

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
        val donatedTotal=binding.donatedTotal
        val donationType=binding.donationType
        val donationDate=binding.donationDate

        if (uid!=null) {
            firebaseReference.collection("users").document(uid)
                .get().addOnSuccessListener {
                    userProfileName.text="Hello "+it?.getString("name")
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }else userProfileName.text="Hello "

        val documentSummaryRef = db.collection("users").document(uid).collection("summary").document(uid)

        if (uid!=null) {
            documentSummaryRef.get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
                val document = task.result

                if (task.isSuccessful) {
                    if (document.exists()) {
                        donatedTotal.text = document.get("donated").toString()
                        donationType.text=document.get("type").toString()
                        donationDate.text=document.get("date").toString()
                    }
//                    firebaseReference.collection("users").document(uid)
//                .collection("summery").document(uid).get().addOnSuccessListener {
//                    donatedTotal.text=it?.getString("donated")
//                    donationType.text=it?.getString("type")
//                    donationDate.text=it?.getString("date")
                }
            }

        }


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