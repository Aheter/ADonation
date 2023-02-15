package com.example.adonation
import android.content.Intent
import android.content.Intent.getIntent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.adonation.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentProfile : Fragment(){

    var imageUri: Uri? = null
    private var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!


    private lateinit var auth:FirebaseAuth
    private lateinit var firebaseReference:FirebaseFirestore
    override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        //get user id
        //val userID = Intent.extras.getString("samplename")

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        //get users Name
        firebaseReference = FirebaseFirestore.getInstance()
        val userProfileName = binding.userProfileName
        if (uid!=null) {
            firebaseReference.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                    if (it!=null) {
                        // Document found in the offline cache
                        userProfileName.setText(it.getString("name"))
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }

        }else userProfileName.setText("nothing")




        val profilePhoto= binding.userProfilePhoto
        profilePhoto.setOnClickListener{

        }




        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        return rootView
    }


}

