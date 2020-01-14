package com.dev.eventsgeofencing.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostRegister {
    @SerializedName("nama")
    @Expose
    var nama: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("kontak")
    @Expose
    var kontak: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null
}