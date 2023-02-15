package com.example.adonation
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.adonation.databinding.FragmentProfileBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class FragmentProfile : Fragment(){
    private val PICKIMAGE_REQUEST = 100
    var imageUri: Uri? = null
    private var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!


    private lateinit var auth:FirebaseAuth
    private lateinit var firebaseReference:FirebaseFirestore
    private lateinit var storage: StorageReference
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        //get user id
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()

        //get user's Name
        firebaseReference = FirebaseFirestore.getInstance()
        val userProfileName = binding.userProfileName

        if (uid!=null) {
            firebaseReference.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                        // Document found in the offline cache
                        userProfileName.text=it?.getString("name")+" Profile"
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }else userProfileName.text="nothing"


        val userProfilePhoto=binding.userProfilePhoto
        userProfilePhoto.setOnClickListener{
            selectFile()
            UploadImage(uid)
        }





        return binding.root
    }

private fun UploadImage(uid:String){
    var fileName = "profile_image"
    storage = FirebaseStorage.getInstance().getReference(uid+"/"+fileName)
    imageUri?.let { storage.putFile(it)
            .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot: UploadTask.TaskSnapshot? ->
                //profilePicture.setImageURI(null);
                Toast.makeText(activity?.applicationContext, "Successfully Uploaded profile photo", Toast.LENGTH_LONG).show()
        }).addOnFailureListener(
                OnFailureListener { e: Exception? ->
                    Toast.makeText(activity?.applicationContext, "Profile photo uploading failure", Toast.LENGTH_SHORT).show()
                })
    }

}
    private fun selectFile() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,PICKIMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            imageUri = data!!.data

            binding.userProfilePhoto.setImageURI(imageUri)
        }
    }

}

