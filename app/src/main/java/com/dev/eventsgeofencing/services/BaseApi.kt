package com.dev.eventsgeofencing.services

import com.dev.eventsgeofencing.BuildConfig
import com.dev.eventsgeofencing.model.Post.PostLogin
import com.dev.eventsgeofencing.model.Post.PostRegister
import com.dev.eventsgeofencing.model.Response.ResponseEvents
import com.dev.eventsgeofencing.model.Response.ResponseLogin
import com.dev.eventsgeofencing.model.Response.ResponseRegister
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
    fun postRegister(
        @Body postRegister : PostRegister?
    ) : Observable<ResponseRegister>

    @POST("json_ShowUser.php")
    fun postLogin(
        @Body postLogin : PostLogin?
    ) : Observable<Response<ResponseLogin>>

    companion object {
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
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(BaseApi::class.java)
        }
    }
}