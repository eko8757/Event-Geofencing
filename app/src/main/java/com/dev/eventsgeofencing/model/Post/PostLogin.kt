package com.dev.eventsgeofencing.model.Post

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostLogin {
    @SerializedName("nama")
    @Expose
    var nama: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null
}