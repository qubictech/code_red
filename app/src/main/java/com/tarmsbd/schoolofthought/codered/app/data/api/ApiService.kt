package com.tarmsbd.schoolofthought.codered.app.data.api

import com.tarmsbd.schoolofthought.codered.app.data.models.Location
import retrofit2.http.POST

interface ApiService {
    @POST("/location")
    suspend fun getLocations(): List<Location>
}