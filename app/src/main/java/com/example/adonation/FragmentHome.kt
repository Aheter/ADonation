package com.example.adonation

import android.app.ProgressDialog
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
import com.example.adonation.data.Post
import com.example.adonation.data.PostsList
import com.example.adonation.databinding.FragmentPostsBinding
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class FragmentHome : Fragment() {
    private var _binding: FragmentPostsBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentPostsBinding.inflate(inflater,container, false)

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
            val now = Date()
            val timeD = formatter.format(now)

           // val formatter = DateTimeFormatter.ofPattern(" dd-MM-yyyy HH:mm")
         //   val timeD= LocalDateTime.now().format(formatter)
            val post = Post("USER", msg, "PIC",timeD)
            PostsList.add(post)
            editText.setText("")
            binding.recyclerView.adapter?.notifyItemInserted(0)
            binding.recyclerView.scrollToPosition(0)


        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter=postAdapter(PostsList.posts )
        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())

        ItemTouchHelper(object :ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            )= makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT )

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                PostsList.remove(viewHolder.adapterPosition)
                binding.recyclerView.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.recyclerView)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}