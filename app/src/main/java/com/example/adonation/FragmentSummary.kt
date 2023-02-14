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

class FragmentSummary : Fragment() {
    private var _binding: FragmentSummaryBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding=FragmentSummaryBinding.inflate(inflater,container, false)
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