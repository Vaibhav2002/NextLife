package com.vaibhav.nextlife.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.vaibhav.nextlife.R
import com.vaibhav.nextlife.ui.auth.AuthorizationActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this, AuthorizationActivity::class.java))
            finish()
        }, 2000L)
    }
}