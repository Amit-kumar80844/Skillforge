package com.example.skillforge.presentation.detail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.skillforge.core.util.UiState
import com.example.skillforge.domain.model.Course
import com.example.skillforge.domain.model.Lesson
import com.example.skillforge.presentation.components.ErrorScreen
import com.example.skillforge.presentation.components.LoadingScreen

// --- Extracted precise colors to match the screenshot ---
private val PrimaryTeal = Color(0xFF2596BE)
private val TealSurface = PrimaryTeal.copy(alpha = 0.12f)
private val TextPrimary = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF757575)
private val StarYellow = Color(0xFFFFB300)
private val BackgroundWhite = Color(0xFFFFFFFF)
private val BorderGray = Color(0xFFEBEBEB)
private val LockGray = Color(0xFF9E9E9E)
private val IconSurface = Color(0xFFF5F5F5)

@Composable
fun CourseDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLesson: (String, String) -> Unit,
    viewModel: CourseDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundWhite,
        bottomBar = {
            if (uiState is UiState.Success) {
                BottomEnrollBar()
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = uiState,
            label = "DetailStateCrossfade",
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) { state ->
            when (state) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Error -> ErrorScreen(message = state.message, onRetry = viewModel::fetchCourseDetail)
                is UiState.Success -> CourseDetailContent(
                    course = state.data,
                    onNavigateBack = onNavigateBack,
                    onLessonSelected = { lessonId -> onNavigateToLesson(state.data.id, lessonId) }
                )
            }
        }
    }
}

@Composable
private fun CourseDetailContent(
    course: Course,
    onNavigateBack: () -> Unit,
    onLessonSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            // The Hero Section handles the gradient background and the top info
            HeroSection(course, onNavigateBack)
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                InstructorCard(course)
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = course.description,
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Course content",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${course.lessons.size} lessons • ${course.lessons.sumOf { it.durationMinutes }} min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        items(course.lessons, key = { it.id }) { lesson ->
            LessonCard(
                lesson = lesson,
                onClick = { onLessonSelected(lesson.id) },
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun HeroSection(course: Course, onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(PrimaryTeal, BackgroundWhite),
                    startY = 0f,
                    endY = 1200f // Creates the smooth, drawn-out fade effect
                )
            )
    ) {
        // Subtle background circle effect
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 80.dp, y = (-20).dp)
                .size(250.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 24.dp) // Adjusted for status bar padding
        ) {
            // Top App Bar Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BackgroundWhite)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BackgroundWhite)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Small Category Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.5f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "// ${course.categoryName.lowercase()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            // Large White Watermark Text
            Text(
                text = course.title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 44.sp,
                    lineHeight = 44.sp,
                    fontWeight = FontWeight.Black
                ),
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )

            // Dynamic Tags
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                course.tags.take(3).forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(TealSurface)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelMedium,
                            color = PrimaryTeal,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Title
            Text(
                text = course.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp
                ),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = course.subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = StarYellow, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = course.rating.toString(), style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                }
                Text(text = "%,d".format(course.studentsEnrolled), style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${course.durationHours}h", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                }

                Text(
                    text = course.level,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryTeal
                )
            }
        }
    }
}

@Composable
private fun InstructorCard(course: Course) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundWhite),
        border = BorderStroke(1.dp, BorderGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PrimaryTeal),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = course.instructor.name.split(" ").map { it.first() }.joinToString(""),
                        color = BackgroundWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = course.instructor.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = course.instructor.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
            TextButton(onClick = { }) {
                Text(
                    text = "Follow",
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryTeal,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun LessonCard(lesson: Lesson, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundWhite),
        border = BorderStroke(1.dp, BorderGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (lesson.isFree) TealSurface else IconSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (lesson.isFree) Icons.Default.PlayArrow else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (lesson.isFree) PrimaryTeal else LockGray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (lesson.isFree) TextPrimary else TextSecondary
                )
                Text(
                    text = "${lesson.durationMinutes} min",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            if (lesson.isFree) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(TealSurface)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "FREE",
                        style = MaterialTheme.typography.labelMedium,
                        color = PrimaryTeal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomEnrollBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = BackgroundWhite,
        shadowElevation = 24.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PRICE",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Text(
                    text = "Free",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryTeal
                )
            }
            Button(
                onClick = { },
                modifier = Modifier
                    .height(56.dp)
                    .weight(1f)
                    .padding(start = 32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
            ) {
                Text(
                    text = "Enroll now",
                    style = MaterialTheme.typography.titleMedium,
                    color = BackgroundWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}