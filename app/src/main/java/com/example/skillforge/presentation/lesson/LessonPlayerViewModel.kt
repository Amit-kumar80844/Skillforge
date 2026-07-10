package com.example.skillforge.presentation.lesson

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.skillforge.core.util.UiState
import com.example.skillforge.domain.model.Course
import com.example.skillforge.domain.model.Lesson
import com.example.skillforge.domain.usecase.GetCategoriesUseCase
import com.example.skillforge.presentation.navigation.LessonPlayerDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LessonPlayerState(
    val course: Course,
    val currentLesson: Lesson
)

@HiltViewModel
class LessonPlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val destination = savedStateHandle.toRoute<LessonPlayerDestination>()
    val courseId = destination.courseId
    private val initialLessonId = destination.lessonId

    private val _uiState = MutableStateFlow<UiState<LessonPlayerState>>(UiState.Loading)
    val uiState: StateFlow<UiState<LessonPlayerState>> = _uiState.asStateFlow()

    init {
        loadLesson()
    }

    fun loadLesson(lessonId: String = initialLessonId) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val categories = getCategoriesUseCase()
                val course = categories.flatMap { it.courses }.find { it.id == courseId }
                
                if (course != null) {
                    val lesson = course.lessons.find { it.id == lessonId }
                    if (lesson != null) {
                        _uiState.value = UiState.Success(LessonPlayerState(course, lesson))
                    } else {
                        _uiState.value = UiState.Error("Lesson not found")
                    }
                } else {
                    _uiState.value = UiState.Error("Course not found")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}
