package com.akira.miokotoba.ui.features.study

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.akira.miokotoba.AppContainer
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.components.MioListItem
import com.akira.miokotoba.ui.components.MioSectionHeader
import com.akira.miokotoba.ui.design.MioSpacing
import com.akira.miokotoba.ui.features.study.components.StudyBookCard
import com.akira.miokotoba.ui.features.study.components.StudyBookPicker
import com.akira.miokotoba.ui.features.study.components.StudyProgressSummary
import com.akira.miokotoba.ui.features.study.components.StudyStartButton
import com.akira.miokotoba.ui.navigation.Screen

@Composable
fun StudyStartPage(
    navController: NavController
) {
    val wordBookRepository = AppContainer.wordBookRepository
    var books by remember { mutableStateOf<List<WordBook>>(emptyList()) }
    var selectedBook by remember { mutableStateOf(books.firstOrNull()) }

    LaunchedEffect(Unit) {
        books = wordBookRepository.getBooks()
        selectedBook = books.firstOrNull()
    }

    var showPicker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        StudyStartContent(
            selectedBook = selectedBook,
            onPickBook = { showPicker = true },
            onStart = {
                selectedBook?.let { book ->
                    navController.navigate(Screen.StudySession.createRoute(book.id))
                }
            },
            onKanaChart = { navController.navigate(Screen.KanaChart.route) }
        )

        StudyBookPicker(
            visible = showPicker,
            books = books,
            selectedBook = selectedBook,
            onDismiss = { showPicker = false },
            onPick = { book ->
                selectedBook = book
                showPicker = false
            }
        )
    }
}

@Composable
private fun StudyStartContent(
    selectedBook: WordBook?,
    onPickBook: () -> Unit,
    onStart: () -> Unit,
    onKanaChart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MioSpacing.pageHorizontal),
        verticalArrangement = Arrangement.spacedBy(MioSpacing.lg)
    ) {
        StudyBookCard(
            book = selectedBook,
            onClick = onPickBook,
            modifier = Modifier.padding(top = MioSpacing.sm)
        )

        StudyProgressSummary(book = selectedBook)

        MioSectionHeader(text = "工具")
        QuickStudyLinks(onKanaChart = onKanaChart)

        Spacer(modifier = Modifier.weight(1f))

        StudyStartButton(
            enabled = selectedBook != null,
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = MioSpacing.lg)
        )
    }
}

@Composable
private fun QuickStudyLinks(onKanaChart: () -> Unit) {
    MioListItem(
        title = "五十音图",
        subtitle = "快速复习平片假名",
        icon = Icons.Rounded.GridView,
        onClick = onKanaChart,
        trailing = {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}
