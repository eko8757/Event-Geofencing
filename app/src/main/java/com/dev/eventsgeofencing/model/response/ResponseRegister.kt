package com.dev.eventsgeofencing.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseRegister {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("token")
    @Expose
    var token: String? = null
}