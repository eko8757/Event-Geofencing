package com.dev.eventsgeofencing.presenter

import android.util.Log
import com.dev.eventsgeofencing.model.post.PostRegister
import com.dev.eventsgeofencing.model.response.ResponseRegister
import com.dev.eventsgeofencing.services.BaseApi
import com.dev.eventsgeofencing.view.OnboardingView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class SignUpPresenter(val view: OnboardingView.SignUpView, val factory: BaseApi) {

    private var mCompisiteDisposable: CompositeDisposable? = null

    fun postRegister(
        nama: String,
        email: String,
        kontak: String,
        password: String
    ) {
        view.showProgress()
        val post = PostRegister()
        post.nama = nama
        post.email = email
        post.kontak = kontak
        post.password = password

        mCompisiteDisposable = CompositeDisposable()
        mCompisiteDisposable?.add(
            factory.postDataRegister(post)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<Response<ResponseRegister>>() {
                    override fun onComplete() {
                        view.hideProgress()
                    }

                    override fun onNext(t: Response<ResponseRegister>) {
                        Log.d("ResultRegister", t.body()?.code.toString())
                        Log.d("ResultRegisterError", t.errorBody().toString())
                        if (t.code() == 200) {
                            view.successRegister()
                            view.hideProgress()
                        } else {
                            view.hideProgress()
                            view.showDialog(t.message().toString())
                        }
                    }

                    override fun onError(e: Throwable) {
                        view.showToast(e.message.toString())
                        view.hideProgress()
                    }
                })
        )
    }
}