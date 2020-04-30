package com.tarmsbd.schoolofthought.codered.app.data.api

import com.tarmsbd.schoolofthought.codered.app.data.models.CodeRedResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/code_red")
    suspend fun getResponse(@Body body: HashMap<String, String>): CodeRedResponse
}