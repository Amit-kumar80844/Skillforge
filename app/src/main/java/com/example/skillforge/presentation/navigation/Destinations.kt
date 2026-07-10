package com.example.skillforge.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeDestination

@Serializable
data class CourseDetailDestination(val courseId: String)

@Serializable
data class LessonPlayerDestination(
    val courseId: String,
    val lessonId: String
)
