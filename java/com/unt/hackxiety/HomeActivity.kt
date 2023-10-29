package com.unt.hackxiety

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = findViewById<TextView>(R.id.textViewGreeting)
        val reguser = sharedPreferences.getString("reguser","suhana")
        username.text = "Hi "+reguser
        val sosButton = findViewById<Button>(R.id.sosImg)
        sosButton.setOnClickListener {
            openAcitvity(this, SOSAcitvity::class.java)
            
        }
        val profile = findViewById<Button>(R.id.imageViewProfile)
        profile.setOnClickListener {
            openAcitvity(this, ProfileActivity::class.java)
           
        }
        val gratitudeView = findViewById<CardView>(R.id.gratitudeJournaling)
        gratitudeView.setOnClickListener {
            openAcitvity(AffirmationsAcitvity::class.java)
           
        }
        val eduView = findViewById<CardView>(R.id.resourcesCard)
        eduView.setOnClickListener {
            openAcitvity(EduActivity::class.java)
         
        }
        val medhelp = findViewById<CardView>(R.id.getHelp)
            medhelp.setOnClickListener {

            openLinkInBrowser("https://www.talkspace.com/")

        }


    }
    private fun openAcitvity(activity:Class){
        val intent = Intent(this, acitvity)
            startActivity(intent)
    }
    private fun openLinkInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}
