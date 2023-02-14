package com.example.adonation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

}