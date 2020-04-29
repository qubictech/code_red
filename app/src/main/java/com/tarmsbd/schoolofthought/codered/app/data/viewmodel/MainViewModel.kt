package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tarmsbd.schoolofthought.codered.app.data.models.CodeRedResponse
import com.tarmsbd.schoolofthought.codered.app.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {
    val repo = MainRepository

    fun getLocations() = liveData(Dispatchers.IO) {
        try {
            emit(repo.locations())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getResponse(body: HashMap<String, String>): LiveData<String> = liveData(Dispatchers.IO) {
        try {
            emit(repo.response(body))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}