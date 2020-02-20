package com.twistentiger.startrak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.core.os.postDelayed

class LauncherActivity : AppCompatActivity()
{
    companion object{
        const val SPLASH_SCREEN: Long = 3500 //setting 3.5 secs till switch
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_launcher)

        //OS handler lets the action look like it's loading and switch to new activities
        Handler().postDelayed({
            val launchIntent = Intent(this@LauncherActivity, SignInActivity::class.java)
            startActivity(launchIntent)
            finish()
        }, SPLASH_SCREEN)

    }
}
