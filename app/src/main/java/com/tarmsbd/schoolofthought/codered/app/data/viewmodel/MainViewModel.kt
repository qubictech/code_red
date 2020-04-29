package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tarmsbd.schoolofthought.codered.app.data.models.RecentStatus
import com.tarmsbd.schoolofthought.codered.app.data.repository.MainRepository

class MainViewModel : ViewModel() {
    val getRecentStatus: LiveData<List<RecentStatus>> = MainRepository.recentStatus
}