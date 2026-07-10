package com.example.skillforge.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillforge.core.util.UiState
import com.example.skillforge.domain.model.Category
import com.example.skillforge.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<String?>(null)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    val homeState: StateFlow<UiState<List<Category>>> = combine(
        _uiState,
        _searchQuery,
        _selectedCategoryId
    ) { state, query, selectedId ->
        when (state) {
            is UiState.Success -> {
                val filteredCategories = state.data.map { category ->
                    val filteredCourses = category.courses.filter { course ->
                        (selectedId == null || category.id == selectedId) &&
                        (query.isBlank() || course.title.contains(query, ignoreCase = true) ||
                                course.tags.any { it.contains(query, ignoreCase = true) })
                    }
                    category.copy(courses = filteredCourses)
                }.filter { it.courses.isNotEmpty() || (selectedId == null && query.isBlank()) }
                
                UiState.Success(filteredCategories)
            }
            else -> state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val categories = getCategoriesUseCase()
                _uiState.value = UiState.Success(categories)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(categoryId: String) {
        _selectedCategoryId.update { current ->
            if (current == categoryId) null else categoryId
        }
    }
}
