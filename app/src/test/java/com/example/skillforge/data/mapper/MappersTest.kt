package com.example.skillforge.data.mapper

import com.example.skillforge.data.dto.CategoryDto
import com.example.skillforge.data.dto.CourseDto
import com.example.skillforge.data.dto.InstructorDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MappersTest {

    @Test
    fun `categoryDto toDomain maps all properties correctly`() {
        // Arrange
        val lessonDto = com.example.skillforge.data.dto.LessonDto(
            id = "les_1",
            title = "Lesson 1",
            durationMinutes = 10,
            isFree = true,
            videoUrl = "https://example.com/video",
            content = "Lesson content"
        )
        
        val instructorDto = InstructorDto(
            id = "inst_1",
            name = "John Doe",
            title = "Expert",
            avatarUrl = "https://example.com/avatar",
            bio = "Bio"
        )
        
        val courseDto = CourseDto(
            id = "course_1",
            title = "Course Title",
            subtitle = "Course Subtitle",
            thumbnailUrl = "https://example.com/thumb",
            level = "Beginner",
            durationHours = 5.5,
            rating = 4.8,
            studentsEnrolled = 1000,
            language = "English",
            lastUpdated = "2026-01-01",
            tags = listOf("Tag1", "Tag2"),
            instructor = instructorDto,
            description = "Course description",
            lessons = listOf(lessonDto)
        )
        
        val categoryDto = CategoryDto(
            id = "cat_1",
            name = "Android Development",
            description = "Android desc",
            iconColor = "#FF0000",
            courseCount = 1,
            courses = listOf(courseDto)
        )

        // Act
        val category = categoryDto.toDomain()

        // Assert
        assertEquals("cat_1", category.id)
        assertEquals("Android Development", category.name)
        assertEquals(1, category.courses.size)
        
        val course = category.courses.first()
        assertEquals("course_1", course.id)
        assertEquals("Android Development", course.categoryName) // Testing the injected categoryName
        assertEquals("John Doe", course.instructor.name)
        
        val lesson = course.lessons.first()
        assertEquals("les_1", lesson.id)
        assertTrue(lesson.isFree)
    }
}
