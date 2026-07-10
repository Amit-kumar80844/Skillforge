package com.example.skillforge.data.mapper

import com.example.skillforge.data.dto.CategoryDto
import com.example.skillforge.data.dto.CourseDto
import com.example.skillforge.data.dto.InstructorDto
import com.example.skillforge.data.dto.LessonDto
import com.example.skillforge.domain.model.Category
import com.example.skillforge.domain.model.Course
import com.example.skillforge.domain.model.Instructor
import com.example.skillforge.domain.model.Lesson

fun CategoryDto.toDomain(): Category = Category(
    id = id,
    name = name,
    description = description,
    iconColor = iconColor,
    courseCount = courseCount,
    courses = courses.map { it.toDomain(categoryName = name) }
)

fun CourseDto.toDomain(categoryName: String): Course = Course(
    id = id,
    title = title,
    subtitle = subtitle,
    thumbnailUrl = thumbnailUrl,
    level = level,
    durationHours = durationHours,
    rating = rating,
    studentsEnrolled = studentsEnrolled,
    language = language,
    lastUpdated = lastUpdated,
    tags = tags,
    instructor = instructor.toDomain(),
    description = description,
    lessons = lessons.map { it.toDomain() },
    categoryName = categoryName
)

fun InstructorDto.toDomain(): Instructor = Instructor(
    id = id,
    name = name,
    title = title,
    avatarUrl = avatarUrl,
    bio = bio
)

fun LessonDto.toDomain(): Lesson = Lesson(
    id = id,
    title = title,
    durationMinutes = durationMinutes,
    isFree = isFree,
    videoUrl = videoUrl,
    content = content
)
