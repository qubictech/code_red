package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tarmsbd.schoolofthought.codered.app.data.models.LoginUser
import com.tarmsbd.schoolofthought.codered.app.data.models.RecentStatus
import com.tarmsbd.schoolofthought.codered.app.data.repository.FirebaseRepo
import com.tarmsbd.schoolofthought.codered.app.data.repository.MainRepository
import java.util.logging.Logger

const val TAG = "RegViewModel"

class AuthViewModel : ViewModel() {
    private var mUser = MutableLiveData<LoginUser>()
    private var user = LoginUser()

    var nameError = MutableLiveData<String>()
    var numberError = MutableLiveData<String>()
    var setIntentName = ""

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

    var password = ""
        set(value) {
            field = value
            user.password = value
            nameError.value = value
            mUser.value = user
        }


    fun loginUser(view: View) {
        Logger.getLogger(TAG).warning(user.toString())
        val error = mutableListOf<String>()

        var valid = true

        if (user.password.isEmpty()) {
            error.add("Password can't be empty")
            valid = false
        }

        if (user.password.isNotEmpty() && user.password.length < 6) {
            error.add("Password should at least 6+ chars")
            valid = false
        }

        if (user.mobile.isEmpty()) {
            error.add("Mobile Number is required")
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
            Toast.makeText(view.context, message, Toast.LENGTH_LONG)
                .show()

            return
        }
    }

    val userCredentialForLogin: LiveData<LoginUser> = mUser

    private fun toast(msg: String, context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    val getRecentStatus: LiveData<List<RecentStatus>> = MainRepository.recentStatus

}