package com.unt.hackxiety.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.unt.hackxiety.HomeActivity
import com.unt.hackxiety.databinding.ActivityLoginBinding

import com.unt.hackxiety.R
import com.unt.hackxiety.RegistrationActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val textViewLogin = findViewById<TextView>(R.id.textViewRegister)

        // Create a SpannableString with "Already have an account? - Log in"
        val spannableString = SpannableString("Yet to create an account? - Sign up")

        // Create a ClickableSpan for "Log in" part
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Handle the click event and navigate to the login activity
                val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
                startActivity(intent)
            }
        }

        // Set the ClickableSpan for "Log in" and make it clickable
        spannableString.setSpan(clickableSpan, 28 ,35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textViewLogin.text = spannableString
        textViewLogin.movementMethod = LinkMovementMethod.getInstance()
        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading
        val forGotPassword = binding.forgotPassword

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val reguser = sharedPreferences.getString("reguser","suhana")
                val regpass = sharedPreferences.getString("regpass","Suhana123")
                if(reguser?.equals(username) == true && regpass?.equals(password) == true){
                    loginViewModel.login(username.text.toString(), password.text.toString())
                    editor.putBoolean("loggedIn", true)
                    editor.apply()
                }
                else{
                    Toast.makeText(
                        applicationContext,
                        "username or password invalid",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            forgotPassword.setOnClickListener {
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
