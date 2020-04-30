package com.tarmsbd.schoolofthought.codered.app.data.models

import com.google.firebase.database.Exclude

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

data class LoginUser(var mobile: String, var password: String){
    constructor():this("","")
}

data class RegisterUser(
    var mobile: String,
    var fullName: String,
    var gender: String,
    var dateOfBirth: String,
    var password: String
) {
    constructor() : this("", "", "", "", "")

    @Exclude
    fun toMap(): Map<String, String> {
        return mapOf(
            "mobile" to mobile,
            "fullName" to fullName,
            "gender" to gender,
            "dateOfBirth" to dateOfBirth,
            "password" to password
        )
    }
}