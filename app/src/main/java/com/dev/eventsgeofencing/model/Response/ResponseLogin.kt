package com.dev.eventsgeofencing.model.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLogin {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("token")
    @Expose
    var token: String? = null
}