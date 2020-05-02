package com.tarmsbd.schoolofthought.codered.app.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tarmsbd.schoolofthought.codered.app.data.models.CodeRedResponse
import com.tarmsbd.schoolofthought.codered.app.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {
    private val repo = MainRepository

    fun getResponse(body: HashMap<String, String>): LiveData<CodeRedResponse> =
        liveData(Dispatchers.IO) {
            try {
                emit(repo.response(body))
            } catch (e: Exception) {
                emit(CodeRedResponse("Failed"))
                e.printStackTrace()
            }
        }
}