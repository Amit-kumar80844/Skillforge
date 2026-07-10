package com.example.skillforge.data.remote

import com.example.skillforge.data.dto.ApiResponse
import retrofit2.http.GET

interface SkillforgeApiService {

    @GET("android-assesment/notes/refs/heads/main/data.json")
    suspend fun getCourseData(): ApiResponse
}
