package com.akira.miokotoba.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akira.miokotoba.AppContainer
import com.akira.miokotoba.ui.components.PlaceholderPage
import com.akira.miokotoba.ui.features.study.KanaChartPage
import com.akira.miokotoba.ui.features.study.StudyPage
import com.akira.miokotoba.ui.features.wordbook.WordBookDetailPage
import com.akira.miokotoba.ui.features.wordbook.WordBookDetailViewModel
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
    val wordBookRepository = AppContainer.wordBookRepository

    val wordBookDetailViewModel: WordBookDetailViewModel = viewModel()
    val wordBookDetailUiState = wordBookDetailViewModel.uiState

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
                LaunchedEffect(bookId) {
                    wordBookDetailViewModel.loadBook(bookId)
                }
                WordBookDetailPage(
                    bookId = bookId,
                    uiState = wordBookDetailUiState,
                    onAddWordClick = wordBookDetailViewModel::onAddWordClick,
                    onDismissAddWordPage = wordBookDetailViewModel::onDismissAddWordPage,
                    onWordAdded = wordBookDetailViewModel::onWordAdded,
                    onDismissWordEditPage = wordBookDetailViewModel::onDismissWordEditPage,
                    onWordUpdated = wordBookDetailViewModel::onWordUpdated,
                    onEditWordClick = wordBookDetailViewModel::onEditWordClick,
                    onDeleteWordClick = wordBookDetailViewModel::onDeleteWordClick,
                    onDismissDeleteDialog = wordBookDetailViewModel::onDismissDeleteDialog,
                    onConfirmDeleteWord = wordBookDetailViewModel::onConfirmDeleteWord,
                    onWordClick = wordBookDetailViewModel::onWordClick,
                    onDismissWordDetail = wordBookDetailViewModel::onDismissWordDetail,
                    onSearchQueryChange = wordBookDetailViewModel::onSearchQueryChange,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.StudySession.route) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
                StudyPage(
                    words = wordBookRepository.getWords(bookId),
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.KanaChart.route) {
                KanaChartPage(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
