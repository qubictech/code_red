package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import android.app.DatePickerDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tarmsbd.schoolofthought.codered.app.data.models.RecentStatus
import com.tarmsbd.schoolofthought.codered.app.data.models.RegistrationForSelf
import com.tarmsbd.schoolofthought.codered.app.data.repository.FirebaseRepo
import com.tarmsbd.schoolofthought.codered.app.data.repository.MainRepository
import java.util.*
import java.util.logging.Logger

const val TAG = "RegViewModel"

class HelpForSelfViewModel : ViewModel() {
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private var mUser = MutableLiveData<RegistrationForSelf>()
    private var user = RegistrationForSelf()

    var nameError = MutableLiveData<String>()
    var numberError = MutableLiveData<String>()

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

    fun setGender(gender: String) {
        user.gender = gender
        mUser.value = user
    }

    fun showDatePicker(view: View) {
        DatePickerDialog(
            view.context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // do stuff
                Log.d(TAG, "DatePickerDialog: $year/$monthOfYear/$dayOfMonth")

                user.dateOfBirth = "$year/$monthOfYear/$dayOfMonth"
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

        FirebaseRepo.registerForSelf(user = user, context = view.context)
    }

    val getRecentStatus: LiveData<List<RecentStatus>> = MainRepository.recentStatus

}