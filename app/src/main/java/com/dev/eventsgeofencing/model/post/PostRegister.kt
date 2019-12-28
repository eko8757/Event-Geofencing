package com.dev.eventsgeofencing.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostRegister(

    @SerializedName("nama")
    @Expose
    var nama: String?,

    @SerializedName("email")
    @Expose
    var email: String?,

    @SerializedName("kontak")
    @Expose
    var kontak: String?,

    @SerializedName("password")
    @Expose
    var password: String?

)