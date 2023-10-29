package com.unt.hackxiety

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.unt.hackxiety.login.LoginActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = findViewById<EditText>(R.id.nameEditText)
        assert(reguser != null) { "reguser should not be null" }
        username.setText(sharedPreferences.getString("reguser",null))
        val email = findViewById<EditText>(R.id.emailEditText)
        assert(email.text.isNotBlank()) { "Email must not be blank" }
        email.setText(sharedPreferences.getString("email",null))
        val phone = findViewById<EditText>(R.id.PhoneEditText)
        phone.setText(sharedPreferences.getString("phone",null))
        val em1 = findViewById<EditText>(R.id.emergencyContact1)
        em1.setText(sharedPreferences.getString("em1",null))
        val em2 = findViewById<EditText>(R.id.emergencyContact2)
        em2.setText(sharedPreferences.getString("em2",null))
        val exitButton = findViewById<ImageButton>(R.id.exitButton)
        exitButton.setOnClickListener {
            finish()
        }
        val logout = findViewById<Button>(R.id.logout)
        logout.setOnClickListener {
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("firstTime", true)
            editor.putBoolean("loggedIn",false)
            editor.putString("reguser",null)
            editor.putString("regpass",null)
            editor.putString("email",null)
            editor.putString("em1",null)
            editor.putString("em2",null)
            editor.apply()
            val intent = Intent(this@ProfileActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }


        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putBoolean("firstTime", false)
            editor.putBoolean("loggedIn",true)
            editor.putString("reguser",username.text.toString())
            editor.putString("email",email.text.toString())
            editor.putString("phone",phone.text.toString())
            editor.putString("em1",em1.text.toString())
            editor.putString("em2",em2.text.toString())
            editor.apply()
            val intent = Intent(this@ProfileActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
