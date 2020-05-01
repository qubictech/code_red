package com.tarmsbd.schoolofthought.codered.app.data.models

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.Exclude
import java.util.*

data class SelfResult(
    var mobile: String,
    var location: LatLng,
    var result: String,
    var answers: Map<String, String>
) {
    constructor() : this("", LatLng(0.0, 0.0), "", mapOf())

    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "mobile" to mobile,
            "location" to location,
            "result" to result,
            "timestamp" to Date().time,
            "answers" to answers
        )
    }
}