package com.dev.eventsgeofencing.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dev.eventsgeofencing.R
import com.dev.eventsgeofencing.utils.StaticString
import com.pixplicity.easyprefs.library.Prefs

class SplashScreen : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private var SPLASH_DELAY: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val token = Prefs.getString(StaticString().TOKEN, "")
            if (token != "") {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            } else {
                val i = Intent(this, LandingPage::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }
}
