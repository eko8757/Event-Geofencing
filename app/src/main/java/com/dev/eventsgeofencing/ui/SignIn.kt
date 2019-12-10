package com.dev.eventsgeofencing.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.dev.eventsgeofencing.R
import com.dev.eventsgeofencing.presenter.LoginPresenter
import com.dev.eventsgeofencing.services.BaseApi
import com.dev.eventsgeofencing.view.OnboardingView
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity(), OnboardingView.SignInView, View.OnClickListener{

    private lateinit var progressDialog: ProgressDialog
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val factory: BaseApi = BaseApi.create()
        presenter = LoginPresenter(this, factory)
        btn_sign_in.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_sign_in -> checkForm()
        }
    }

    override fun showProgress() {
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait..")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    override fun hideProgress() {
        progressDialog.dismiss()
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun checkForm() {
        val username = ed_username_login.text.toString()
        val password = ed_password_login.text.toString()
        presenter.postLogin(username, password)
    }

    override fun successLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun showDialog(result: String) {
        val builder = AlertDialog.Builder(this, R.style.CustomDialogTheme)
        val view = layoutInflater.inflate(R.layout.layout_custom_dialog, null)
        val button = view.findViewById<Button>(R.id.btn_dialog)
        val title = view.findViewById<TextView>(R.id.tv_title_dialog)
        val content = view.findViewById<TextView>(R.id.tv_content_dialog)
        builder.setView(view)

        title.setText("Oops!")
        content.setText(result)

        val alert: AlertDialog = builder.create()
        alert.show()
        alert.setCanceledOnTouchOutside(true)

        button.setOnClickListener {
            alert.dismiss()
            ed_username_login.setText("")
            ed_password_login.setText("")
        }
    }

}
