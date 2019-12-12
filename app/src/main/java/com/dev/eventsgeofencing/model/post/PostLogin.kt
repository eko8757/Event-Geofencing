package com.dev.eventsgeofencing.model.post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostLogin(
    @SerializedName("nama")
    @Expose
    var nama: String?,

    @SerializedName("password")
    @Expose
    var password: String?
)