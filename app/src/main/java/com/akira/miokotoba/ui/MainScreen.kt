package com.akira.miokotoba.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akira.miokotoba.ui.components.PlaceholderPage
import com.akira.miokotoba.model.SampleData
import com.akira.miokotoba.ui.features.study.StudyPage
import com.akira.miokotoba.ui.features.wordbook.WordBookDetailPage
import com.akira.miokotoba.ui.navigation.Screen
import com.akira.miokotoba.ui.navigation.parallaxEnterFromLeft
import com.akira.miokotoba.ui.navigation.parallaxExitToLeft
import com.akira.miokotoba.ui.navigation.slideInFromRight
import com.akira.miokotoba.ui.navigation.slideOutToRight

/**
 * 应用主屏幕 — 裸 NavHost，无外层 Scaffold
 * 每个路由自己负责自己的 TopBar / BottomBar
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            enterTransition = { slideInFromRight() },
            exitTransition = { parallaxExitToLeft() },
            popEnterTransition = { parallaxEnterFromLeft() },
            popExitTransition = { slideOutToRight() },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            composable(Screen.Main.route) {
                MainTabsScreen(navController = navController)
            }
            composable(Screen.WordBookDetail.route) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
                val book = SampleData.bookById(bookId) ?: return@composable
                WordBookDetailPage(
                    wordBook = book,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.WordAdd.route) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
                WordBookDetailPage(
                    wordBook = SampleData.bookById(bookId) ?: return@composable,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.StudySession.route) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
                StudyPage(words = SampleData.wordsForBook(bookId))
            }
            composable(Screen.KanaChart.route) {
                PlaceholderPage("五十音图")
            }
        }
    }
}
