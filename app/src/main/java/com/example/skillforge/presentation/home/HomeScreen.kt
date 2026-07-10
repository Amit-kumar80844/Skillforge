package com.example.skillforge.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.skillforge.core.util.UiState
import com.example.skillforge.domain.model.Category
import com.example.skillforge.domain.model.Course
import com.example.skillforge.presentation.components.ErrorScreen
import com.example.skillforge.presentation.components.LoadingScreen
import com.example.skillforge.theme.*

@Composable
fun HomeScreen(
    onNavigateToCourse: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.homeState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Crossfade(
            targetState = uiState,
            label = "HomeStateCrossfade",
            modifier = Modifier.padding(innerPadding)
        ) { state ->
            when (state) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Error -> ErrorScreen(message = state.message, onRetry = viewModel::fetchData)
                is UiState.Success -> HomeContent(
                    categories = state.data,
                    searchQuery = searchQuery,
                    selectedCategoryId = selectedCategoryId,
                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                    onCategorySelected = viewModel::onCategorySelected,
                    onCourseClick = onNavigateToCourse
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    categories: List<Category>,
    searchQuery: String,
    selectedCategoryId: String?,
    onSearchQueryChanged: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onCourseClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            HeaderSection(modifier = Modifier.padding(horizontal = 16.dp))
        }
        item {
            SearchSection(searchQuery, onSearchQueryChanged, modifier = Modifier.padding(horizontal = 16.dp))
        }
        if (categories.isNotEmpty()) {
            item {
                CategoriesSection(
                    categories = categories,
                    selectedCategoryId = selectedCategoryId,
                    onCategorySelected = onCategorySelected
                )
            }
            item {
                SectionHeader("Popular courses", "See all", modifier = Modifier.padding(horizontal = 16.dp))
            }
            itemsIndexed(
                items = categories.flatMap { it.courses }.distinctBy { it.id },
                key = { _, course -> course.id }
            ) { index, course ->
                CourseCard(
                    course = course,
                    index = index,
                    onClick = { onCourseClick(course.id) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        } else {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                    Text("No courses found", color = TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Find your next skill",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(White)
                    .border(1.dp, DividerColor, CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications", tint = TextPrimary, modifier = Modifier.size(24.dp))
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(TealDark),
                contentAlignment = Alignment.Center
            ) {
                Text("AS", color = White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun SearchSection(query: String, onQueryChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = { Text("Search courses, topics...", color = TextTertiary) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextTertiary) },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Teal,
            unfocusedBorderColor = DividerColor,
            focusedContainerColor = White,
            unfocusedContainerColor = White
        ),
        singleLine = true
    )
}

@Composable
private fun SectionHeader(title: String, actionText: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Text(text = actionText, style = MaterialTheme.typography.labelLarge, color = TealDark)
    }
}

@Composable
private fun CategoriesSection(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategorySelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader("Categories", "See all", modifier = Modifier.padding(horizontal = 16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories, key = { it.id }) { category ->
                val isSelected = selectedCategoryId == category.id
                CategoryCard(
                    category = category,
                    isSelected = isSelected,
                    onClick = { onCategorySelected(category.id) }
                )
            }
        }
        // Scrollbar indicator mock
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack, 
                contentDescription = null, 
                tint = TextTertiary, 
                modifier = Modifier.size(16.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(TextTertiary)
            )
            Icon(
                imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowForward, 
                contentDescription = null, 
                tint = TextTertiary, 
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun CategoryCard(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    val containerColor = if (isSelected) TealSurface else White
    val borderColor = if (isSelected) Teal else DividerColor

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(android.graphics.Color.parseColor(category.iconColor)).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(android.graphics.Color.parseColor(category.iconColor)))
                )
            }
            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${category.courseCount} courses",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun CourseThumbnail(course: Course, index: Int, modifier: Modifier = Modifier) {
    val colorPalettes = listOf(
        Color(0xFF14B8A6), // Teal
        Color(0xFF6366F1), // Purple
        Color(0xFF10B981), // Green
        Color(0xFFF59E0B)  // Orange
    )
    
    // User requested both "each color in 2 sections" and "differ after each section".
    // Following the provided image (Teal -> Purple -> Teal) which shows alternating colors.
    val baseColor = colorPalettes[index % colorPalettes.size]

    Box(
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(baseColor, baseColor.copy(alpha = 0.85f)),
                    start = Offset(0f, 0f),
                    end = Offset(300f, 300f)
                )
            )
    ) {
        // Dot Grid Pattern - Exact matching from image
        Canvas(modifier = Modifier.fillMaxSize()) {
            val dotSpacing = 8.dp.toPx()
            val dotRadius = 0.8.dp.toPx()
            val offsetX = (size.width % dotSpacing) / 2
            val offsetY = (size.height % dotSpacing) / 2
            
            for (x in 0 until (size.width / dotSpacing).toInt() + 1) {
                for (y in 0 until (size.height / dotSpacing).toInt() + 1) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.12f),
                        radius = dotRadius,
                        center = Offset(offsetX + x * dotSpacing, offsetY + y * dotSpacing)
                    )
                }
            }
        }

        // Decorative circles - Exact placement as per screenshot
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 35.dp, y = (-25).dp)
                .size(90.dp)
                .background(Color.White.copy(alpha = 0.15f), CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 30.dp, y = 35.dp)
                .size(80.dp)
                .background(Color.White.copy(alpha = 0.15f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Badge: White background with base color text
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "/ ${course.categoryName.lowercase()}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = baseColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            // Title: Bold White at the bottom
            Text(
                text = course.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    color = Color.White
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CourseCard(course: Course, index: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val levelColor = when (course.level.lowercase()) {
        "beginner" -> BeginnerColor
        "intermediate" -> IntermediateColor
        else -> AdvancedColor
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(124.dp) // Adjusted height for better vertical balance
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp), // Slightly more rounded as per modern UI
        colors = CardDefaults.cardColors(containerColor = White),
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            CourseThumbnail(course = course, index = index)
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = course.level.uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = levelColor
                )
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = course.instructor.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextTertiary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = StarYellow, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = course.rating.toString(), style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(androidx.compose.material.icons.Icons.Default.AccessTime, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${course.durationHours}h", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                }
            }
        }
    }
}
