package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import android.app.DatePickerDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tarmsbd.schoolofthought.codered.app.data.models.RegisterUser
import com.tarmsbd.schoolofthought.codered.app.data.repository.FirebaseRepo
import java.util.*
import java.util.logging.Logger

class RegistrationViewModel : ViewModel() {
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private var mUser = MutableLiveData<RegisterUser>()
    private var user = RegisterUser()
    private var mConfirmPassword: String = ""

    var nameError = MutableLiveData<String>()
    var numberError = MutableLiveData<String>()
    var passwordError = MutableLiveData<String>()

    init {
        nameError.value = " "
        numberError.value = " "
    }

    var getUser = mUser

    var mobile = ""
        set(value) {
            field = value
            user.mobile = value
            numberError.value = value
            mUser.value = user
        }

    var fullName = ""
        set(value) {
            field = value
            user.fullName = value
            nameError.value = value
            mUser.value = user
        }

    var password = ""
        set(value) {
            field = value
            user.password = value
            passwordError.value = value
            mUser.value = user
        }

    var confirmPassword = ""
        set(value) {
            field = value
            mConfirmPassword = value
        }

    fun setGender(gender: String) {
        user.gender = gender
        mUser.value = user
    }

    fun showDatePicker(view: View) {
        DatePickerDialog(
            view.context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // do stuff
                Log.d(TAG, "DatePickerDialog: $year/${monthOfYear + 1}/$dayOfMonth")

                user.dateOfBirth = "$year/${monthOfYear + 1}/$dayOfMonth"
                mUser.value = user
            },
            year,
            month,
            day
        ).show()
    }

    fun createUser(view: View) {
        Logger.getLogger(TAG).warning(user.toString())
        val error = mutableListOf<String>()

        var valid = true
        if (user.gender == "NONE") {
            error.add("Gender")
            valid = false
        }

        if (user.dateOfBirth.isEmpty()) {
            error.add("Birth Date")
            valid = false
        }

        if (user.fullName.isEmpty()) {
            error.add("Name")
            valid = false
        }

        if (user.mobile.isEmpty()) {
            error.add("Mobile Number")
            valid = false
        }

        if (user.password.isEmpty()) {
            error.add("Password")
            valid = false
        }

        if (user.password.isNotEmpty()) {
            if (user.password != mConfirmPassword) {
                error.add("Password Not Matched")
                valid = false
            }
        }

        if (user.password.isNotEmpty() && user.password.length < 6) {
            error.add("Password should at least 6+ chars")
            valid = false
        }

        if (!valid) {
            var message = ""
            if (error.size > 1) {
                for (e in error) {
                    message += "$e,"
                }

                message = message.substring(0, message.length - 1)
            } else message = error[0]
            Toast.makeText(view.context, "Please enter your $message", Toast.LENGTH_LONG)
                .show()

            return
        }

        FirebaseRepo.registerUser(user, view.context)
    }
}
