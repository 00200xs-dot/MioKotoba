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
import androidx.navigation.NavController
import com.akira.miokotoba.ui.components.PlaceholderPage
import com.akira.miokotoba.ui.components.navigation.MioNavigationBar
import com.akira.miokotoba.ui.components.topbar.MioTopBar
import com.akira.miokotoba.ui.components.topbar.TopBarMode
import com.akira.miokotoba.ui.features.wordbook.WordBookPage
import com.akira.miokotoba.ui.navigation.BottomNavItem
import com.akira.miokotoba.ui.navigation.Screen

/**
 * 主页 Tab 容器
 * 包含自己的 Scaffold + TopBar + BottomNav，Tab 切换在内部用 when 处理
 */
@Composable
fun MainTabsScreen(navController: NavController) {
    var selectedTab by rememberSaveable { mutableStateOf(BottomNavItem.Study) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var topBarMode by remember { mutableStateOf<TopBarMode>(TopBarMode.Focus) }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
        contentColor = MaterialTheme.colorScheme.onBackground,

        bottomBar = {
            MioNavigationBar(
                selectedItem = selectedTab,
                onScreenSelected = { tab ->
                    if (selectedTab != tab) {
                        searchQuery = ""
                        topBarMode = if (tab == BottomNavItem.Study) TopBarMode.Focus else TopBarMode.Default
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
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it }
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
                    searchQuery = searchQuery,
                    onBookClick = { book ->
                        navController.navigate(Screen.WordBookDetail.createRoute(book.id))
                    }
                )
                BottomNavItem.Study -> PlaceholderPage("学习入口")
                BottomNavItem.Settings -> PlaceholderPage("设置")
            }
        }
    }
}
