package com.example.skillforge.domain.repository

import com.example.skillforge.domain.model.Category

interface CourseRepository {
    suspend fun getCategories(): List<Category>
}
