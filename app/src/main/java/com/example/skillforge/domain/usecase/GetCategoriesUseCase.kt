package com.example.skillforge.domain.usecase

import com.example.skillforge.domain.model.Category
import com.example.skillforge.domain.repository.CourseRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(): List<Category> {
        return repository.getCategories()
    }
}
