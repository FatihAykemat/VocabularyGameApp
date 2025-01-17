package com.example.vocabularygame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vocabularygame.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding:ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        binding.userid.text= "User Id :: $userId"
        binding.email.text= "Email :: $emailId"
          binding.logout.setOnClickListener {
              logOut()
          }



    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}