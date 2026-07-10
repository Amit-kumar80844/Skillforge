package com.example.skillforge.data.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("meta") val meta: MetaDto,
    @SerializedName("categories") val categories: List<CategoryDto>
)

data class MetaDto(
    @SerializedName("app") val app: String,
    @SerializedName("version") val version: String,
    @SerializedName("generatedAt") val generatedAt: String
)

data class CategoryDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("iconColor") val iconColor: String,
    @SerializedName("courseCount") val courseCount: Int,
    @SerializedName("courses") val courses: List<CourseDto>
)

data class CourseDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String,
    @SerializedName("level") val level: String,
    @SerializedName("durationHours") val durationHours: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("studentsEnrolled") val studentsEnrolled: Int,
    @SerializedName("language") val language: String,
    @SerializedName("lastUpdated") val lastUpdated: String,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("instructor") val instructor: InstructorDto,
    @SerializedName("description") val description: String,
    @SerializedName("lessons") val lessons: List<LessonDto>
)

data class InstructorDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("title") val title: String,
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("bio") val bio: String
)

data class LessonDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("durationMinutes") val durationMinutes: Int,
    @SerializedName("isFree") val isFree: Boolean,
    @SerializedName("videoUrl") val videoUrl: String,
    @SerializedName("content") val content: String
)
