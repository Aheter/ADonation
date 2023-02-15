package com.example.adonation
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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

        var fileName = "profile_image"
        storage = FirebaseStorage.getInstance().getReference(uid)

        val userProfilePhoto=binding.userProfilePhoto

        storage.child("profile_image").downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .into(userProfilePhoto)
        }.addOnFailureListener {
            Toast.makeText(activity?.applicationContext, "Failed to upload picture from storage", Toast.LENGTH_SHORT).show()

           }

            userProfilePhoto.setOnClickListener{
                selectFile()
        }




        return binding.root
    }



private fun UploadImage(){
    val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    var fileName = "profile_image"
    storage = FirebaseStorage.getInstance().getReference(uid+"/"+fileName)
    imageUri?.let { storage.putFile(it)
            .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot: UploadTask.TaskSnapshot? ->
                Toast.makeText(activity?.applicationContext, "Successfully Uploaded profile photo", Toast.LENGTH_LONG).show()
        }).addOnFailureListener(
                OnFailureListener { e: Exception? ->
                    Toast.makeText(activity?.applicationContext, "Profile photo uploading failure", Toast.LENGTH_SHORT).show()
                })
        //binding.userProfilePhoto.setImageURI(imageUri)
        Glide.with(this)
            .load(imageUri)
            .into(binding.userProfilePhoto)
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
            UploadImage()
        }


    }
    fun refreshFragment(context: Context?){
        context?.let{
            val fragmentManager=(context as? AppCompatActivity)?.supportFragmentManager
            fragmentManager?.let {
                val curentFragment = fragmentManager.findFragmentById(R.id.container)
            }
        }
    }

}

