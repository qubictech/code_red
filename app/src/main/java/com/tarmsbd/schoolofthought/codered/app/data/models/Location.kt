package com.tarmsbd.schoolofthought.codered.app.data.models

data class Location(
    var latitude: Double,
    var longitude: Double
) {
    constructor() : this(0.0, 0.0)
}