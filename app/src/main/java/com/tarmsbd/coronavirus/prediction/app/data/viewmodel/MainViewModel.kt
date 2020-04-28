package com.tarmsbd.coronavirus.prediction.app.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tarmsbd.coronavirus.prediction.app.data.models.RecentStatus
import com.tarmsbd.coronavirus.prediction.app.data.repository.MainRepository

class MainViewModel : ViewModel() {
    val getRecentStatus: LiveData<List<RecentStatus>> = MainRepository.recentStatus
}