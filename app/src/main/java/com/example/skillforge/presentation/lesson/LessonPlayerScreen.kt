package com.example.skillforge.presentation.lesson

import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.skillforge.core.util.UiState
import com.example.skillforge.domain.model.Course
import com.example.skillforge.domain.model.Lesson
import com.example.skillforge.presentation.components.ErrorScreen
import com.example.skillforge.presentation.components.LoadingScreen
import com.example.skillforge.theme.*

@Composable
fun LessonPlayerScreen(
    onNavigateBack: () -> Unit,
    viewModel: LessonPlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Crossfade(
            targetState = uiState,
            label = "LessonStateCrossfade",
            modifier = Modifier.padding(innerPadding)
        ) { state ->
            when (state) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Error -> ErrorScreen(message = state.message, onRetry = { viewModel.loadLesson() })
                is UiState.Success -> LessonContent(
                    course = state.data.course,
                    currentLesson = state.data.currentLesson,
                    onNavigateBack = onNavigateBack,
                    onLessonSelected = { viewModel.loadLesson(it) }
                )
            }
        }
    }
}

@Composable
private fun LessonContent(
    course: Course,
    currentLesson: Lesson,
    onNavigateBack: () -> Unit,
    onLessonSelected: (String) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Lessons", "Notes", "Resources")

    Column(modifier = Modifier.fillMaxSize()) {
        FakeVideoPlayer(onNavigateBack,course)
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = "LESSON ${course.lessons.indexOf(currentLesson) + 1} • ${course.title.uppercase()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = TealDark,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentLesson.title,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = currentLesson.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = Teal,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Teal
                        )
                    },
                    divider = { HorizontalDivider(color = DividerColor) }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (selectedTabIndex == index) TextPrimary else TextSecondary
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (selectedTabIndex == 0) {
                items(course.lessons, key = { it.id }) { lesson ->
                    PlayableLessonCard(
                        lesson = lesson,
                        isPlaying = lesson.id == currentLesson.id,
                        onClick = { onLessonSelected(lesson.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                        Text("Coming soon", color = TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun FakeVideoPlayer(onNavigateBack: () -> Unit,course: Course,) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(PlayerBackground)
    ) {
        // Large circular watermark in top right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = (-40).dp)
                .size(200.dp)
                .clip(CircleShape)
                .background(White.copy(alpha = 0.05f))
        )
        
        // Huge Title Watermark
        Text(
            text = course.title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 32.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.ExtraBold
            ),
            color = White.copy(alpha = 0.12f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 42.dp)
        )

        // Back Button
        Surface(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .statusBarsPadding()
                .size(40.dp),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.4f),
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Fullscreen Button
        Surface(
            onClick = { },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .statusBarsPadding()
                .size(40.dp),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.4f),
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Fullscreen,
                    contentDescription = "Fullscreen",
                    tint = White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Play Button
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(White)
                .align(Alignment.Center)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = PlayerBackground, modifier = Modifier.size(32.dp))
        }

        // Progress Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "02:14",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = White
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                // Background Track
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.5.dp)
                        .clip(CircleShape)
                        .background(White.copy(alpha = 0.25f))
                )
                
                // Progress
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.42f)
                        .height(2.5.dp)
                        .clip(CircleShape)
                        .background(Teal)
                )
                
                // Thumb
                Box(
                    modifier = Modifier.fillMaxWidth(0.42f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(White, CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "06:00",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = White
            )
        }
    }
}

@Composable
private fun PlayableLessonCard(lesson: Lesson, isPlaying: Boolean, onClick: () -> Unit) {
    val containerColor = if (isPlaying) TealSurface else White
    val borderColor = if (isPlaying) TealOverlay else DividerColor
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick, enabled = lesson.isFree || isPlaying),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
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
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isPlaying) Teal else if (lesson.isFree) TealSurface else DividerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        isPlaying -> Icons.Default.Pause
                        lesson.isFree -> Icons.Default.PlayArrow
                        else -> Icons.Default.Lock
                    },
                    contentDescription = null,
                    tint = if (isPlaying) White else if (lesson.isFree) TealDark else TextTertiary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isPlaying) TealDark else if (lesson.isFree) TextPrimary else TextTertiary
                )
                Text(
                    text = if (isPlaying) "Now playing • ${lesson.durationMinutes} min" else "${lesson.durationMinutes} min",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isPlaying) TealOverlay else TextSecondary
                )
            }
            if (lesson.isFree && !isPlaying) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(TealSurface)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "FREE",
                        style = MaterialTheme.typography.labelMedium,
                        color = TealDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
