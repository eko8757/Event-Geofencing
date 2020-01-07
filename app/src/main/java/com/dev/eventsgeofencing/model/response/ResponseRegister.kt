package com.dev.eventsgeofencing.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseRegister (
    @SerializedName("code")
    @Expose
    val code: String,
    @SerializedName("message")
    @Expose
    val message: String
)