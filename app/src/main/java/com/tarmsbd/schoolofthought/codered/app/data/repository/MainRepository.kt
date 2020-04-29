package com.tarmsbd.schoolofthought.codered.app.data.repository

import androidx.lifecycle.MutableLiveData
import com.tarmsbd.schoolofthought.codered.app.data.models.RecentStatus

object MainRepository {
    private val dummyData = mutableListOf(
        RecentStatus("Time", "Gender", "Age", "Area"),
        RecentStatus("Today", "M", "43", "Dhaka"),
        RecentStatus("Today", "M", "26", "Dhaka"),
        RecentStatus("Today", "F", "33", "Dhaka")
    )

    private var mRecentStatus = MutableLiveData<List<RecentStatus>>()

    init {
        mRecentStatus.value = dummyData
    }

    val recentStatus = mRecentStatus


}