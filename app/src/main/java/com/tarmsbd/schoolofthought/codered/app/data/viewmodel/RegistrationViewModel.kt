package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import android.app.DatePickerDialog
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout
import com.tarmsbd.schoolofthought.codered.app.data.models.RegisterUser
import com.tarmsbd.schoolofthought.codered.app.utils.MyPatterns
import java.util.*

class RegistrationViewModel : ViewModel() {
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private var mUser = MutableLiveData<RegisterUser>()
    private var user = RegisterUser()
    private var mConfirmPassword: String = ""

    val getUser = mUser

    init {
        user.gender = "M"
        mUser.value = user
    }

    var mobile = ""
        set(value) {
            field = value
            user.mobile = value
            mUser.value = user
        }

    var fullName = ""
        set(value) {
            field = value
            user.fullName = value
            mUser.value = user
        }

    var password = ""
        set(value) {
            field = value
            user.password = value
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

    val userDataObserver: LiveData<RegisterUser> = mUser

    companion object {

        @JvmStatic
        @BindingAdapter("android:errorInput")
        fun errorInput(textInputLayout: TextInputLayout, string: String) {
            if (string.isEmpty()) {
                textInputLayout.error = "This field is required!"
            } else textInputLayout.error = null
        }

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

        @JvmStatic
        @BindingAdapter("android:errorBirthDate")
        fun errorBirthDate(textView: TextView, string: String) {
            if (string.isEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = "Birth date is required!"
            } else {
                textView.visibility = View.INVISIBLE
            }
        }
    }

}
