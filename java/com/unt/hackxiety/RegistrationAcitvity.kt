class RegistrationActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initialize UI components
        initializeUIComponents()

        // Handle registration button click
        setupRegistrationButton()
    }

    private fun initializeUIComponents() {
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
    }

    private fun setupLoginLink() {
        val textViewLogin = findViewById<TextView>(R.id.textViewLogin)
        val spannableString = createClickableSpan("Already have an account? - Log in") {
            openLoginActivity()
        }
        textViewLogin.text = spannableString
        textViewLogin.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun createClickableSpan(text: String, onClickAction: () -> Unit): SpannableString {
        val spannableString = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClickAction()
            }
        }
        spannableString.setSpan(clickableSpan, text.indexOf("Log in"), text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    private fun setupRegistrationButton() {
        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (validateInputs(name, email, password)) {
                if (registerUser(name, email, password)) {
                    onRegistrationSuccess()
                } else {
                    onRegistrationFailure()
                }
            }
        }
    }

    private fun validateInputs(name: String, email: String, password: String): Boolean {
    
        return true
    }

    private fun onRegistrationSuccess() {
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
        openHomeActivity()
    }

    private fun onRegistrationFailure() {
        Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
    }

    private fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun registerUser(name: String, email: String, password: String): Boolean {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("firstTime", false)
        editor.putBoolean("loggedIn", true)
        editor.putString("reguser", name)
        editor.putString("regpass", password)
        editor.putString("email", email)
        editor.apply()
        return true
    }
}
