package com.dev.eventsgeofencing.services

import com.dev.eventsgeofencing.model.post.PostLogin
import com.dev.eventsgeofencing.model.post.PostRegister
import com.dev.eventsgeofencing.model.response.ResponseEvents
import com.dev.eventsgeofencing.model.response.ResponseLogin
import com.dev.eventsgeofencing.model.response.ResponseRegister
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface BaseApi {

    @GET("json_ShowEvent.php")
    fun getData(
    ) : Observable<ResponseEvents>

    @POST("json_addUser.php")
    fun postDataRegister(
        @Body postRegister : PostRegister?
    ) : Observable<Response<ResponseRegister>>

    @POST("json_ShowUser.php")
    fun postDataLogin(
        @Body postLogin : PostLogin?
    ) : Observable<Response<ResponseLogin>>

    companion object {
        var URL: String = "https://ichaamelia.com/"
        fun create(): BaseApi {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val clientBuilder = OkHttpClient.Builder()
            clientBuilder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()
            val client = clientBuilder.build()
            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(BaseApi::class.java)
        }
    }
}