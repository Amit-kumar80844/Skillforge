package com.example.skillforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skillforge.presentation.detail.CourseDetailScreen
import com.example.skillforge.presentation.home.HomeScreen
import com.example.skillforge.presentation.lesson.LessonPlayerScreen
import com.example.skillforge.presentation.navigation.CourseDetailDestination
import com.example.skillforge.presentation.navigation.HomeDestination
import com.example.skillforge.presentation.navigation.LessonPlayerDestination
import com.example.skillforge.theme.SkillforgeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkillforgeTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = HomeDestination
                ) {
                    composable<HomeDestination> {
                        HomeScreen(
                            onNavigateToCourse = { courseId ->
                                navController.navigate(CourseDetailDestination(courseId))
                            }
                        )
                    }

                    composable<CourseDetailDestination> {
                        CourseDetailScreen(
                            onNavigateBack = { navController.navigateUp() },
                            onNavigateToLesson = { courseId, lessonId ->
                                navController.navigate(LessonPlayerDestination(courseId, lessonId))
                            }
                        )
                    }

                    composable<LessonPlayerDestination> {
                        LessonPlayerScreen(
                            onNavigateBack = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }
}
