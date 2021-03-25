package com.vaibhav.nextlife.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vaibhav.nextlife.R
import com.vaibhav.nextlife.ui.auth.AuthorizationActivity
import com.vaibhav.nextlife.ui.homeScreen.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            if (viewModel.isLoggedIn())
                startActivity(Intent(this, HomeActivity::class.java))
            else
                startActivity(Intent(this, AuthorizationActivity::class.java))
            finish()
        }, 1500L)
    }
}