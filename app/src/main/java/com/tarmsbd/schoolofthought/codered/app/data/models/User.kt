package com.tarmsbd.schoolofthought.codered.app.data.models

data class User(
    var mobile: String,
    var fullName: String,
    var gender: String,
    var dateOfBirth: String
) {
    constructor():this("","","","")
}