package com.akira.miokotoba.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.akira.miokotoba.ui.components.PlaceholderPage
import com.akira.miokotoba.ui.components.navigation.MioNavigationBar
import com.akira.miokotoba.ui.components.topbar.MioTopBar
import com.akira.miokotoba.ui.components.topbar.TopBarMode
import com.akira.miokotoba.ui.features.study.StudyStartPage
import com.akira.miokotoba.ui.features.wordbook.WordBookPage
import com.akira.miokotoba.ui.features.wordbook.WordBookViewModel
import com.akira.miokotoba.ui.navigation.BottomNavItem
import com.akira.miokotoba.ui.navigation.Screen

/**
 * 主页 Tab 容器
 * 包含自己的 Scaffold + TopBar + BottomNav，Tab 切换在内部用 when 处理
 */
@Composable
fun MainTabsScreen(navController: NavController) {
    var selectedTab by rememberSaveable { mutableStateOf(BottomNavItem.Study) }
    var topBarMode by remember { mutableStateOf<TopBarMode>(TopBarMode.Focus) }

    val wordBookViewModel: WordBookViewModel = viewModel()
    val wordBookUiState = wordBookViewModel.uiState

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
        contentColor = MaterialTheme.colorScheme.onBackground,

        bottomBar = {
            MioNavigationBar(
                selectedItem = selectedTab,
                onScreenSelected = { tab ->
                    if (selectedTab != tab) {
                        wordBookViewModel.onSearchQueryChange("")
                        topBarMode =
                            if (tab == BottomNavItem.Study) TopBarMode.Focus else TopBarMode.Default
                    }
                    selectedTab = tab
                }
            )
        },
        topBar = {
            MioTopBar(
                mode = topBarMode,
                onModeChange = { topBarMode = it },
                title = selectedTab.label,
                searchQuery = wordBookUiState.searchQuery,
                onQueryChange = wordBookViewModel::onSearchQueryChange
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTab) {
                BottomNavItem.WordBook -> WordBookPage(
                    uiState = wordBookUiState,
                    onBookClick = { book ->
                        navController.navigate(Screen.WordBookDetail.createRoute(book.id))
                    },
                    onAddBookClick = wordBookViewModel::onAddBookClick,
                    onDismissAddSheet = wordBookViewModel::onDismissAddSheet,
                    onNewBookTitleChange = wordBookViewModel::onNewBookTitleChange,
                    onNewBookDescriptionChange = wordBookViewModel::onNewBookDescriptionChange,
                    onCreateBook = wordBookViewModel::onCreateBook
                )

                BottomNavItem.Study -> StudyStartPage(
                    navController = navController
                )

                BottomNavItem.Settings -> PlaceholderPage("设置")
            }
        }
    }
}
