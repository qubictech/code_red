package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tarmsbd.schoolofthought.codered.app.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {
    val repo = MainRepository
    fun getLocations() = liveData(Dispatchers.IO) {
        try {
            emit(repo.locations())
        } catch (e: Exception) {
            emit("error: " + e.localizedMessage)
        }
    }
}