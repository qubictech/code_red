package com.tarmsbd.schoolofthought.codered.app.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("Latitude")
    @Expose
    var latitude: Double? = null,

    @SerializedName("Area")
    @Expose
    var area: String? = null,

    @SerializedName("Id")
    @Expose
    var id: Int? = null,

    @SerializedName("Longitude")
    @Expose
    var longitude: Double? = null,

    @SerializedName("Status")
    @Expose
    var status: String? = null

)