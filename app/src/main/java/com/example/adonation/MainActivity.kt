package com.example.adonation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

var timer: Timer? = null
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
      timer = Timer()
      timer!!.schedule(object : TimerTask() {
          override fun run() {
              val intent = Intent(this@MainActivity, LogIn::class.java)
              startActivity(intent)
              finish()
          }
      }, 2000)

  }
    }
