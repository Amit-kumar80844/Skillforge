package com.example.skillforge.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.skillforge.core.util.UiState
import com.example.skillforge.domain.model.Course
import com.example.skillforge.domain.usecase.GetCategoriesUseCase
import com.example.skillforge.presentation.navigation.CourseDetailDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val destination = savedStateHandle.toRoute<CourseDetailDestination>()
    val courseId = destination.courseId

    private val _uiState = MutableStateFlow<UiState<Course>>(UiState.Loading)
    val uiState: StateFlow<UiState<Course>> = _uiState.asStateFlow()

    init {
        fetchCourseDetail()
    }

    fun fetchCourseDetail() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val categories = getCategoriesUseCase()
                val course = categories.flatMap { it.courses }.find { it.id == courseId }
                if (course != null) {
                    _uiState.value = UiState.Success(course)
                } else {
                    _uiState.value = UiState.Error("Course not found")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }
}
