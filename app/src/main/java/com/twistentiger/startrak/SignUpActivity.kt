package com.twistentiger.startrak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser

@Suppress("NAME_SHADOWING")
class SignUpActivity : AppCompatActivity()
{
    private lateinit var emailEdit: TextInputEditText
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var progressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailEdit = findViewById(R.id.emailSignUp_editText)
        passwordEdit = findViewById(R.id.passwordSignUp_editText)
        progressBar = findViewById(R.id.signUp_progressBar)
        mAuth = FirebaseAuth.getInstance()

        val signInPrompt: Button = findViewById(R.id.signIn_promptButton)
        val signUpBtn: Button = findViewById(R.id.signUp_button)

        signUpBtn.setOnClickListener {
            registerUser()
        }

        signInPrompt.setOnClickListener {
            val signInPromptIntent = Intent(this@SignUpActivity,
                SignInActivity::class.java)

            this@SignUpActivity.startActivity(signInPromptIntent)
        }
    }

    private fun registerUser()
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

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                progressBar.visibility = View.GONE
                if(task.isSuccessful) //if task is successful, do the following code
                {
                    //send verification email when successful
                    mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener{task ->
                        if(task.isSuccessful)
                        {
                            val user = mAuth.currentUser
                            updateUI(user)
                            Toast.makeText(applicationContext, "Registration Successful. Check email for verification", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            Toast.makeText(applicationContext, task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else
                {
                    if(task.exception is FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(applicationContext,
                            "This account already exists", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(applicationContext,
                            task.exception!!.message, Toast.LENGTH_SHORT).show()

                        updateUI(null)
                    }
                }
            }
    }

    private fun updateUI(user: FirebaseUser?)
    {
        //if the user is not null we move to activity
        //else get. there must be an else
        if(user != null)
        {
            val signUpIntent = Intent(this@SignUpActivity,
                MainActivity::class.java)

            signUpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this@SignUpActivity.startActivity(signUpIntent)
        }
        else
        {
            Toast.makeText(applicationContext,
                "Invalid operation contact developer at twistentiger@gmail.com ", Toast.LENGTH_LONG).show()
        }
    }
}
