package com.tarmsbd.schoolofthought.codered.app.data.models

data class RegistrationForSelf(
    var mobile: String,
    var fullName: String,
    var gender: String,
    var dateOfBirth: String
) {
    constructor() : this("", "", "", "")
}

data class RegistrationForOther(var fullName: String, var gender: String, var age: String) {
    constructor() : this("", "", "")
}