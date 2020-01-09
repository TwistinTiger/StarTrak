package com.twistentiger.startrak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignInActivity : AppCompatActivity()
{
    private lateinit var emailEdit: TextInputEditText
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var progressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        emailEdit = findViewById(R.id.emailSignIn_editText)
        passwordEdit = findViewById(R.id.passwordSignIn_editText)
        progressBar = findViewById(R.id.signIn_progressBar)
        mAuth = FirebaseAuth.getInstance()

        val signUpPrompt: Button = findViewById(R.id.signUp_promptButton)
        val loginBtn: Button = findViewById(R.id.login_button)

        loginBtn.setOnClickListener {
            userSignInWithEmailAndPassword()
        }

        signUpPrompt.setOnClickListener {
            val signUpPromptIntent = Intent(this@SignInActivity,
                SignUpActivity::class.java)
            this@SignInActivity.startActivity(signUpPromptIntent)
        }
    }

    private fun  userSignInWithEmailAndPassword()
    {
        val email: String =  emailEdit.text.toString().trim()
        val password: String = passwordEdit.text.toString().trim()

        if(email.isEmpty())
        {
            emailEdit.error = "Required"
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEdit.error = "Please enter a valid email"
            emailEdit.requestFocus()
            return
        }

        if(password.isEmpty())
        {
            passwordEdit.error = "Required"
            return
        }

        if(password.length < 6)
        {
            passwordEdit.error = "Minimum length of password should be 6"
            passwordEdit.requestFocus()
            return
        }

        progressBar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE

                //if the task is successful we login
                //else we get a fail message
                if(task.isSuccessful)
                {
                    val user = mAuth.currentUser
                    updateUI(user)
                }
                else
                {
                    Toast.makeText(applicationContext,
                        task.exception!!.message, Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?)
    {
        //if the user is not null we move to activity
        //else show error that user doesn't exist or is disabled
        if(user != null)
        {
            val signIntent = Intent(this@SignInActivity,
                MainActivity::class.java)
            signIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this@SignInActivity.startActivity(signIntent)
        }
        else
        {
            Toast.makeText(applicationContext,
                "User doesn't exist or User is disabled", Toast.LENGTH_LONG).show()
        }
        //need to work on a user is disabled function
    }
}
