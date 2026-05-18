package com.akira.miokotoba.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.PlaceholderPage
import com.akira.miokotoba.ui.features.study.StudyPage
import com.akira.miokotoba.ui.components.navigation.MioNavigationBar
import com.akira.miokotoba.ui.components.topbar.MioTopBar
import com.akira.miokotoba.ui.navigation.Navigation

@Composable
fun MainScreen() {
    var selectedScreen by rememberSaveable { mutableStateOf(Navigation.Study.ordinal) }
    var isSearchMode by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val currentScreen = Navigation.entries[selectedScreen]
    var isStudyScreen = currentScreen == Navigation.Study

    Scaffold(
        contentColor = MaterialTheme.colorScheme.onBackground,

        bottomBar = {
            MioNavigationBar(
                selectedScreen = selectedScreen,
                onScreenSelected = { selectedScreen = it }
            )
        },
        topBar = {
            MioTopBar(
                title = Navigation.entries[selectedScreen].label,
                isSearchMode = isSearchMode,
                onSearchModeChange = { isSearchMode = it },
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it },
                isStudyScreen = isStudyScreen,
            ) {
                //预留
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
        ) {
            when (Navigation.entries[selectedScreen]) {
                Navigation.Study -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) { StudyPage() }

                Navigation.Library -> PlaceholderPage("单词库")
                Navigation.Settings -> PlaceholderPage("设置")
            }
        }
    }
}