package com.example.adonation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NewDonation: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        // loadFragment(FragmentHome())
    }
}



//fun getDaysAgo(daysAgo: Int): Date {
//    val calendar = Calendar.getInstance()
//    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
//
//    return calendar.time
//}