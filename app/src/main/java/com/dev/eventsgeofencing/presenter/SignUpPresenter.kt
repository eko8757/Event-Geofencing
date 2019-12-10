package com.dev.eventsgeofencing.presenter

import android.util.Log
import com.dev.eventsgeofencing.model.Post.PostRegister
import com.dev.eventsgeofencing.model.Response.ResponseRegister
import com.dev.eventsgeofencing.services.BaseApi
import com.dev.eventsgeofencing.view.OnboardingView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class SignUpPresenter(val view: OnboardingView.SignUpView, val factory: BaseApi) {

    private var mCompositeDisposable: CompositeDisposable? = null

    fun postData(nama: String, email: String, kontak: String, password: String) {
        view.showProgress()
        val dataRegister = PostRegister()
        dataRegister.nama = nama
        dataRegister.email = email
        dataRegister.kontak = kontak
        dataRegister.password = password
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            factory.postRegister(dataRegister)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<ResponseRegister>() {
                    override fun onComplete() {
                        view.hideProgress()
                    }

                    override fun onNext(t: ResponseRegister) {
                        if (t.code == "200") {
                            view.successRegister()
                            view.hideProgress()
                        } else {
                            view.hideProgress()
                            view.showDialog(t.message.toString())
                        }
                    }

                    override fun onError(e: Throwable) {
                        view.hideProgress()
                        view.showToast(e.message.toString())
                    }
                })
        )
    }
}