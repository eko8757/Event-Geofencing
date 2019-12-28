package com.dev.eventsgeofencing.view

interface OnboardingView {

    interface SignInView {
        fun showProgress()
        fun hideProgress()
        fun showToast(msg: String)
        fun checkForm()
        fun successLogin()
        fun showDialog(result: String)
    }

    interface SignUpView {
        fun showProgress()
        fun hideProgress()
        fun showToast(msg: String)
        fun checkForm()
        fun successRegister()
        fun showDialog(result: String)
    }

    interface LandingView {
        fun buttonLogin()
        fun buttonSignUp()
    }
}