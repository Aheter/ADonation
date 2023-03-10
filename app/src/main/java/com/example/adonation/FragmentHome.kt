package com.example.adonation

import android.app.ProgressDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
//import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.example.adonation.data.FavoriteList
import com.example.adonation.data.Post
import com.example.adonation.data.PostsList
import com.example.adonation.databinding.FragmentPostsBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class FragmentHome : Fragment() {
    private var _binding: FragmentPostsBinding?=null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    var imageUrl: String? = ""
    private lateinit var fstore: FirebaseFirestore
    val db= Firebase.firestore
    private lateinit var firebaseReference:FirebaseFirestore
    private lateinit var storage: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentPostsBinding.inflate(inflater,container, false)
        auth = FirebaseAuth.getInstance()
        firebaseReference = FirebaseFirestore.getInstance()
        val uid = auth.currentUser?.uid.toString()
        var userName=""
        if (uid!=null) {
            firebaseReference.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                    // Document found in the offline cache

                    userName = it?.getString("name").toString()
                }

        }

        storage = FirebaseStorage.getInstance().getReference(uid)


        storage.child("profile_image").downloadUrl.addOnSuccessListener {
            imageUrl=it?.toString()
        }.addOnFailureListener {
            Toast.makeText(activity?.applicationContext, "Failed to upload picture from storage", Toast.LENGTH_SHORT).show()

        }

        val rView =binding.recyclerView
        val editText = binding.editText
        val postBtn = binding.post

        postBtn.setOnClickListener {
            val msg = editText.text.toString()
            if (msg.isEmpty()) {
                Toast.makeText(context, "Please enter text.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val formatter = SimpleDateFormat(" dd-MM-yyyy hh:mm")
            val now = Calendar.getInstance().time
            val timeD = formatter.format(now)

            val post = Post(userName, msg, imageUrl.toString(), false, timeD)
            //load posts to firebase
            UploadPost(post)
            editText.setText("")
            binding.recyclerView.adapter?.notifyItemInserted(0)
            binding.recyclerView.scrollToPosition(0)


        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mutableList : MutableList<Post> = ArrayList()

        val documentpostsRef = db.collection("posts")
            .orderBy("postTime" ,  Query.Direction.DESCENDING)
        documentpostsRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val post = Post(document.get("userName").toString()
                        ,document.get("textInPost").toString()
                        ,document.get("post_image").toString().orEmpty()
                        ,document.get("favorite").toString().toBoolean()
                        ,document.get("postTime").toString())
                    mutableList.add(post)
                    binding.recyclerView.adapter?.notifyItemInserted(0)
                    binding.recyclerView.scrollToPosition(0)
                }

            }
                binding.recyclerView.adapter = postAdapter(mutableList)
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

                ItemTouchHelper(object : ItemTouchHelper.Callback() {
                    override fun getMovementFlags(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder
                    ) = makeFlag(
                        ItemTouchHelper.ACTION_STATE_SWIPE,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    )

                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val post = PostsList.posts[viewHolder.adapterPosition]
                        if (post.isFavorite) {
                            FavoriteList.remove(PostsList.posts[viewHolder.adapterPosition])
                        }
                        PostsList.remove(viewHolder.adapterPosition)
                        binding.recyclerView.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
                    }
                }).attachToRecyclerView(binding.recyclerView)

            }



    fun getRandomString(length: Int=9) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun UploadPost(post:Post){
        auth = FirebaseAuth.getInstance()
        val userID = auth.currentUser?.uid.toString()
        val postID=getRandomString()
        val documentReference = db.collection("posts").document(postID)


        documentReference.set(post).addOnSuccessListener { documentReference ->
            Toast.makeText(context, "Donation added successfully.",Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error adding document.",Toast.LENGTH_SHORT).show()
            }

        binding.recyclerView.adapter?.notifyItemInserted(0)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}