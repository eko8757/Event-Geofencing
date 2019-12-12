package com.dev.eventsgeofencing.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class ResponseEvents {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("event")
    @Expose
    var event: List<Event>? = null

    @Parcelize
    data class Event(
        @SerializedName("id")
        @Expose
        var id: String? = null,

        @SerializedName("nama")
        @Expose
        var nama: String? = null,

        @SerializedName("email")
        @Expose
        var email: String? = null,

        @SerializedName("kontak")
        @Expose
        var kontak: String? = null,

        @SerializedName("tanggal")
        @Expose
        var tanggal: String? = null,

        @SerializedName("latitude")
        @Expose
        var latitude: String? = null,

        @SerializedName("longitude")
        @Expose
        var longitude: String? = null,

        @SerializedName("keterangan")
        @Expose
        var keterangan: String? = null,

        @SerializedName("poster")
        @Expose
        var poster: String? = null
    ) : Parcelable
}