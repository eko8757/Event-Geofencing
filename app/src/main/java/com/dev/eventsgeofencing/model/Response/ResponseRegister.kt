package com.dev.eventsgeofencing.model.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseRegister {
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}