package com.dev.eventsgeofencing.presenter

import com.dev.eventsgeofencing.model.Response.ResponseEvents
import com.dev.eventsgeofencing.services.BaseApi
import com.dev.eventsgeofencing.view.EventsView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class EventsPresenter(val view: EventsView.EventsList, private val factory: BaseApi) {

    private var mCompositeDisposable: CompositeDisposable? = null
    private var eventList: ArrayList<ResponseEvents.Event>? = null

    fun getData() {
        view.showProgress()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            factory.getData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<ResponseEvents>() {
                    override fun onComplete() {
                        view.hideProgress()
                    }

                    override fun onNext(t: ResponseEvents) {
                        view.showData(t.event as ArrayList<ResponseEvents.Event>)
                        eventList = if (t.event == null) {
                            ArrayList()
                        } else {
                            t.event as ArrayList<ResponseEvents.Event>
                        }

                        view.hideProgress()
                    }

                    override fun onError(e: Throwable) {
                        view.showToast(e.message.toString())
                        view.hideProgress()
                    }
                })
        )
    }
}