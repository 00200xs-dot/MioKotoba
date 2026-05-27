package com.akira.miokotoba.ui

import androidx.activity.compose.BackHandler
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
import com.akira.miokotoba.PlaceholderPage
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.components.navigation.MioNavigationBar
import com.akira.miokotoba.ui.components.topbar.MioTopBar
import com.akira.miokotoba.ui.components.topbar.TopBarMode
import com.akira.miokotoba.ui.features.study.StudyPage
import com.akira.miokotoba.ui.features.wordbook.WordBookDetailPage
import com.akira.miokotoba.ui.features.wordbook.WordBookPage
import com.akira.miokotoba.ui.navigation.BottomNavItem

@Composable
fun MainScreen() {
    var selectedItem by rememberSaveable { mutableStateOf(BottomNavItem.Study) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var topBarMode by remember { mutableStateOf<TopBarMode>(TopBarMode.Focus) }
    // 当前打开的词书详情页
    var currentDetailBook: WordBook? by rememberSaveable { mutableStateOf(null) }

    // 拦截系统返回键：详情页打开时返回列表，而不是退出 APP
    BackHandler(enabled = currentDetailBook != null) {
        currentDetailBook = null
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
        contentColor = MaterialTheme.colorScheme.onBackground,

        bottomBar = {
            if (currentDetailBook == null) {
                MioNavigationBar(
                    selectedItem = selectedItem,
                    onScreenSelected = {
                        if (selectedItem != it) {
                            searchQuery = ""
                            topBarMode = if (it == BottomNavItem.Study) {
                                TopBarMode.Focus
                            } else {
                                TopBarMode.Default
                            }
                        }
                        selectedItem = it
                    }
                )
            }
        },
        topBar = {
            if (currentDetailBook == null) {
                MioTopBar(
                    mode = topBarMode,
                    onModeChange = { topBarMode = it },
                    title = selectedItem.label,
                    searchQuery = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
        ) {
            when {
                (currentDetailBook != null) -> WordBookDetailPage(
                    wordBook = currentDetailBook!!,
                    onBack = { currentDetailBook = null }
                )

                selectedItem == BottomNavItem.Study -> StudyPage()
                selectedItem == BottomNavItem.WordBook -> WordBookPage(
                    searchQuery = searchQuery,
                    onBookClick = { currentDetailBook = it })

                selectedItem == BottomNavItem.Settings -> PlaceholderPage("设置")
            }
        }
    }
}