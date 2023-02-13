package com.example.adonation
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
class FragmentSummary : Fragment() {
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_summary, container, false)

//        val donation = rootView.findViewById<Button>(R.id.addDonation)
//        donation.setOnClickListener{
//            val intent = Intent(this,NewDonation::class.java)
//            startActivity(intent)
//        }

        return rootView
    }


}