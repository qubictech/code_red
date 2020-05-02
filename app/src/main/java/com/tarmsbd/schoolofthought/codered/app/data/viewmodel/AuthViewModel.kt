package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout
import com.tarmsbd.schoolofthought.codered.app.data.models.LoginUser
import com.tarmsbd.schoolofthought.codered.app.utils.MyPatterns
import java.util.logging.Logger

const val TAG = "RegViewModel"

class AuthViewModel : ViewModel() {
    private var mUser = MutableLiveData<LoginUser>()
    private var user = LoginUser()

    var getUser = mUser

    var mobile = ""
        set(value) {
            field = value
            user.mobile = value
            mUser.value = user
        }

    var password = ""
        set(value) {
            field = value
            user.password = value
            mUser.value = user
        }

    companion object {
        @JvmStatic
        @BindingAdapter("android:errorMobile")
        fun errorMobile(textInputLayout: TextInputLayout, string: String) {
            if (string.isEmpty()) {
                textInputLayout.error = "Mobile Number is required!"
            } else {
                if (!MyPatterns.NUMBER_PATTERN.matches(string))
                    textInputLayout.error =
                        "Please enter valid mobile number" else textInputLayout.error = null
            }
        }

        @JvmStatic
        @BindingAdapter("android:errorPassword")
        fun errorPassword(textInputLayout: TextInputLayout, string: String) {
            if (string.isEmpty()) {
                textInputLayout.error = "Password is required!"
            } else {
                if (string.length < 6)
                    textInputLayout.error =
                        "Password should be at least 6+ chars" else textInputLayout.error = null
            }
        }
    }

    val userCredentialForLogin: LiveData<LoginUser> = mUser

    private fun toast(msg: String, context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}