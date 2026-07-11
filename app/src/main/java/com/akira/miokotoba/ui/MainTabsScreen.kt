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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.akira.miokotoba.R
import com.akira.miokotoba.ui.components.PlaceholderPage
import com.akira.miokotoba.ui.components.navigation.MioNavigationBar
import com.akira.miokotoba.ui.components.topbar.MioTopBar
import com.akira.miokotoba.ui.components.topbar.MioTopBarAction
import com.akira.miokotoba.ui.components.topbar.MioTopBarActionStyle
import com.akira.miokotoba.ui.components.topbar.MioTopBarActionType
import com.akira.miokotoba.ui.components.topbar.MioTopBarSearchState
import com.akira.miokotoba.ui.components.topbar.MioTopBarState
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

    val wordBookViewModel: WordBookViewModel = viewModel()

    LaunchedEffect(Unit) {
        wordBookViewModel.refreshBooks()
    }

    val wordBookUiState = wordBookViewModel.uiState

    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    val topBarState = when (selectedTab) {
        BottomNavItem.WordBook -> MioTopBarState(
            title = selectedTab.label,
            actions = listOf(
                MioTopBarAction(
                    iconRes = R.drawable.ic_topbar_search,
                    contentDescription = "搜索单词本",
                    type = MioTopBarActionType.Search,
                    style = MioTopBarActionStyle.Filled
                )
            ),
            searchState = MioTopBarSearchState(
                query = wordBookUiState.searchQuery,
                active = isSearchActive
            )
        )

        BottomNavItem.Study -> MioTopBarState(
            title = selectedTab.label,
            actions = listOf(
                MioTopBarAction(
                    iconRes = R.drawable.ic_nav_settings_settings,
                    contentDescription = "学习设置",
                    type = MioTopBarActionType.Settings
                )
            )
        )

        BottomNavItem.Settings -> MioTopBarState(
            title = selectedTab.label
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
        contentColor = MaterialTheme.colorScheme.onBackground,

        bottomBar = {
            MioNavigationBar(
                selectedItem = selectedTab,
                onScreenSelected = { tab ->
                    if (selectedTab != tab) {
                        isSearchActive = false
                        wordBookViewModel.onSearchQueryChange("")
                        selectedTab = tab
                    }
                }
            )
        },
        topBar = {
            MioTopBar(
                state = topBarState,
                onActionClick = { actionType ->
                    when (actionType) {
                        MioTopBarActionType.Search -> isSearchActive = true
                        MioTopBarActionType.Settings -> {/* 打开学习设置页面 */
                        }

                        MioTopBarActionType.More -> Unit
                    }
                },
                onSearchQueryChange = wordBookViewModel::onSearchQueryChange,
                onSearchDismiss = {
                    isSearchActive = false
                    wordBookViewModel.onSearchQueryChange("")
                }
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
                    onCreateBook = wordBookViewModel::onCreateBook,
                    onEditBookClick = wordBookViewModel::onEditBookClick,
                    onDeleteBookClick = wordBookViewModel::onDeleteBookClick,
                    onDismissDeleteDialog = wordBookViewModel::onDismissDeleteDialog,
                    onConfirmDeleteBook = wordBookViewModel::onConfirmDeleteBook
                )

                BottomNavItem.Study -> StudyStartPage(
                    navController = navController
                )

                BottomNavItem.Settings -> PlaceholderPage("设置")
            }
        }
    }
}
