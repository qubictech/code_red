package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import android.app.DatePickerDialog
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tarmsbd.schoolofthought.codered.app.data.models.RecentStatus
import com.tarmsbd.schoolofthought.codered.app.data.models.User
import com.tarmsbd.schoolofthought.codered.app.data.repository.MainRepository
import java.util.*
import java.util.logging.Logger

const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private var mUser = MutableLiveData<User>()
    private var user = User()

    var getUser = mUser

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
    }

    val getRecentStatus: LiveData<List<RecentStatus>> = MainRepository.recentStatus

}