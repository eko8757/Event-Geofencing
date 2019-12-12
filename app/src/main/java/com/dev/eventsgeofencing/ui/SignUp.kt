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
import com.dev.eventsgeofencing.presenter.SignUpPresenter
import com.dev.eventsgeofencing.services.BaseApi
import com.dev.eventsgeofencing.utils.ConnectionDetector
import com.dev.eventsgeofencing.view.OnboardingView
import kotlinx.android.synthetic.main.activity_sign_up.*

@Suppress("DEPRECATION")
class SignUp : AppCompatActivity(), OnboardingView.SignUpView, View.OnClickListener {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var presenter: SignUpPresenter
    private lateinit var connectionDetector: ConnectionDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val factory: BaseApi = BaseApi.create()
        presenter = SignUpPresenter(this, factory)
        btn_sign_up.setOnClickListener(this)
        connectionDetector = ConnectionDetector()
        connectionDetector.isConnectingToInternet(context = this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_sign_up -> checkForm()
        }
    }

    override fun checkForm() {
        val nama = ed_username.text.toString()
        val email = ed_email.text.toString()
        val kontak = ed_kontak.text.toString()
        val password = ed_password.text.toString()
        if (nama.isEmpty() && email.isEmpty() && kontak.isEmpty() && password.isEmpty()) {
            showToast("Field cannot be empty")
        } else {
            if (connectionDetector.isConnectingToInternet(context = this)) {
                presenter.postData(nama, email, kontak, password)
            } else {
                showToast("Connection lost")
            }
        }
    }

    override fun successRegister() {
        if (connectionDetector.isConnectingToInternet(context = this)) {
            val i = Intent(this, SignIn::class.java)
            startActivity(i)
            finish()
        } else {
            showToast("Connection lost")
        }
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
            ed_username.setText("")
            ed_email.setText("")
            ed_kontak.setText("")
            ed_password.setText("")
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

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}
