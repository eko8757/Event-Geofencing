package com.dev.eventsgeofencing.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dev.eventsgeofencing.R
import com.dev.eventsgeofencing.view.OnboardingView
import kotlinx.android.synthetic.main.activity_landing_page.*

class LandingPage : AppCompatActivity(), OnboardingView.LandingView, View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        btn_sign_in.setOnClickListener(this)
        btn_sign_up.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_sign_in -> buttonLogin()
            R.id.btn_sign_up -> buttonSignUp()
        }
    }

    override fun buttonLogin() {
        val i = Intent(this, SignIn::class.java)
        startActivity(i)
    }

    override fun buttonSignUp() {
        val i = Intent(this, SignUp::class.java)
        startActivity(i)
    }
}
