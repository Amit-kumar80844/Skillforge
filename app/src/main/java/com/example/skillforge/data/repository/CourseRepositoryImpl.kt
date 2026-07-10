package com.example.skillforge.data.repository

import com.example.skillforge.data.mapper.toDomain
import com.example.skillforge.data.remote.SkillforgeApiService
import com.example.skillforge.domain.model.Category
import com.example.skillforge.domain.repository.CourseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepositoryImpl @Inject constructor(
    private val apiService: SkillforgeApiService
) : CourseRepository {

    override suspend fun getCategories(): List<Category> {
        val response = apiService.getCourseData()
        return response.categories.map { it.toDomain() }
    }
}
