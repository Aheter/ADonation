package com.example.adonation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adonation.data.FavoriteList
import com.example.adonation.data.PostsList
import com.example.adonation.databinding.FavoriteBinding

class FragmentFavorite: Fragment() {
    private var _binding: FavoriteBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FavoriteBinding.inflate(inflater,container, false)

        val rView =binding.frecycler

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.frecycler.adapter=FavsAdapter(FavoriteList.posts )
        binding.frecycler.layoutManager= LinearLayoutManager(requireContext())

        ItemTouchHelper(object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            )= makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT )

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                FavoriteList.posts[viewHolder.adapterPosition].isFavorite=false
                FavoriteList.remove(viewHolder.adapterPosition)
                binding.frecycler.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.frecycler)
        //binding.frecycler.adapter?.notifyItemInserted(0)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}