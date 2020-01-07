package com.dev.eventsgeofencing.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostRegister(

    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("kontak")
    @Expose
    val kontak: String,

    @SerializedName("nama")
    @Expose
    val nama: String,

    @SerializedName("password")
    @Expose
    val password: String
)