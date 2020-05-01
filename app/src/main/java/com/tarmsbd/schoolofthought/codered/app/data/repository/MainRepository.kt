package com.tarmsbd.schoolofthought.codered.app.data.repository

import com.tarmsbd.schoolofthought.codered.app.data.api.RetrofitBuilder
import com.tarmsbd.schoolofthought.codered.app.data.models.CodeRedResponse

object MainRepository {
    suspend fun response(body: HashMap<String, String>): CodeRedResponse = RetrofitBuilder.apiService.getResponse(body)
}