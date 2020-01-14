package com.dev.eventsgeofencing.presenter

import android.util.Log
import com.dev.eventsgeofencing.model.post.PostLogin
import com.dev.eventsgeofencing.model.response.ResponseLogin
import com.dev.eventsgeofencing.services.BaseApi
import com.dev.eventsgeofencing.utils.StaticString
import com.dev.eventsgeofencing.view.OnboardingView
import com.pixplicity.easyprefs.library.Prefs
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class LoginPresenter(
    val view: OnboardingView.SignInView,
    private val factory: BaseApi
) {
    private var mCompositeDisposable: CompositeDisposable? = null

    fun postLogin(nama: String, password: String) {

        view.showProgress()
        val dataLogin = PostLogin(nama, password)
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            factory.postDataLogin(dataLogin)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<Response<ResponseLogin>>() {
                    override fun onComplete() {
                        view.hideProgress()
                    }

                    override fun onNext(t: Response<ResponseLogin>) {
                        if (t.code() == 200) {
                            val resultToken = t.body()?.token.toString()
                            Prefs.putString(StaticString().TOKEN, resultToken)
                            view.successLogin()
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
                })
        )
    }
}