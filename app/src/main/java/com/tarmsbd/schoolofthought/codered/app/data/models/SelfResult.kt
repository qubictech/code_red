package com.tarmsbd.schoolofthought.codered.app.data.models

import com.google.firebase.database.Exclude
import java.util.*

data class SelfResult(
    var mobile: String,
    var location: Location,
    var result: String,
    var timestamp: Long = 0,
    var answers: Map<String, String>
) {
    constructor() : this("", Location(), "", 0, mapOf())

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