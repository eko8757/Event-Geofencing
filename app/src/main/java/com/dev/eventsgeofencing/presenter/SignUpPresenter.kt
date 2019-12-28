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

class SignUpPresenter(val view: OnboardingView.SignUpView, var factory: BaseApi) {

    private var mCompositeDisposable: CompositeDisposable? = null

    fun postData(namaResult: String, emailResult: String, kontakResult: String, passwordResult: String) {
        view.showProgress()
        val postRegister = PostRegister(namaResult, emailResult, kontakResult, passwordResult)
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            factory.postDataRegister(postRegister)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<Response<ResponseRegister>>() {
                    override fun onComplete() {
                        view.hideProgress()
                    }

                    override fun onNext(t: Response<ResponseRegister>) {
                        Log.d("ResponseRegister", t.code().toString())
                        if (t.code() == 200) {
                            view.successRegister()
                            view.hideProgress()
                        } else {
                            view.hideProgress()
                            view.showDialog(t.body()?.message.toString())
                        }
                    }

                    override fun onError(e: Throwable) {
                        view.hideProgress()
                        view.showToast(e.message.toString())
                    }
                }
                )
        )
    }
}