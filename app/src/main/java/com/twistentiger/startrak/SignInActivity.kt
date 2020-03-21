package com.twistentiger.startrak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.*

class SignInActivity : AppCompatActivity()
{
    private lateinit var emailEdit: TextInputEditText
    private lateinit var passwordEdit: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var googleSignInButton: SignInButton

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        emailEdit = findViewById(R.id.emailSignIn_editText)
        passwordEdit = findViewById(R.id.passwordSignIn_editText)
        progressBar = findViewById(R.id.signIn_progressBar)
        googleSignInButton = findViewById(R.id.googleSignIn_button)

        val signUpPrompt: Button = findViewById(R.id.signUp_promptButton)
        val loginBtn: Button = findViewById(R.id.login_button)

        //Start google config_signIn
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        //End config_signIn

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {
            userSignInWithEmailAndPassword()
        }

        signUpPrompt.setOnClickListener {
            val signUpPromptIntent = Intent(this@SignInActivity,
                SignUpActivity::class.java)
            this@SignInActivity.startActivity(signUpPromptIntent)
        }

        googleSignInButton.setOnClickListener {
            val googleSignIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(googleSignIntent, RC_SIGN_IN)
        }

    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN)
        {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try
            {
                val account = task.getResult(ApiException::class.java)

                if (account != null)
                {
                    userSignInWithGoogle(account)
                }
            }
            catch(e: ApiException)
            {
                Toast.makeText(applicationContext, e.message,
                    Toast.LENGTH_SHORT).show()

                updateUI(null)
            }
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
                    //check if the user has verified their email
                    if(mAuth.currentUser!!.isEmailVerified)
                    {
                        updateUI(mAuth.currentUser)
                    }
                    else
                    {
                        Toast.makeText(applicationContext,
                            "Please verify your email address, to login",
                            Toast.LENGTH_SHORT).show()

                        updateUI(null)
                    }

                }
                else
                {
                    Toast.makeText(applicationContext,
                        task.exception!!.message, Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun userSignInWithGoogle(account: GoogleSignInAccount)
    {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)

        progressBar.visibility = View.VISIBLE
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE

                if(task.isSuccessful)
                {
                    updateUI(mAuth.currentUser)
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
        //need to work on a user is disabled function
    }

    /*
    When user presses the back button from Signin activity
    the app does not bring them back to the activity it shuts off
     */
    @Override
    override fun onBackPressed()
    {
        val closeAppIntent = Intent(Intent.ACTION_MAIN)
        closeAppIntent.addCategory(Intent.CATEGORY_HOME)
        closeAppIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(closeAppIntent)
        super.onBackPressed()
    }

    companion object{
        const val RC_SIGN_IN:Int = 7004
    }
}
