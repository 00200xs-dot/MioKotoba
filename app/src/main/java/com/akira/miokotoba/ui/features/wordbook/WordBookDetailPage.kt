package com.akira.miokotoba.ui.features.wordbook

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.akira.miokotoba.R
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.ui.animation.MioMotion
import com.akira.miokotoba.ui.design.MioSize
import com.akira.miokotoba.ui.design.MioSpacing
import com.akira.miokotoba.ui.features.study.components.WordCardBase
import com.akira.miokotoba.ui.features.wordbook.components.WordEntryCard
import com.akira.miokotoba.ui.modifier.blurIf
import com.akira.miokotoba.ui.modifier.tiltOnTouch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordBookDetailPage(
    uiState: WordBookDetailUiState,
    onAddWordClick: () -> Unit,
    onDismissAddWordPage: () -> Unit,
    onWordAdded: (Word) -> Unit,
    onWordClick: (Word) -> Unit,
    onDismissWordDetail: () -> Unit,
    onBack: () -> Unit
) {
    val wordBook = uiState.wordBook ?: return
    var displayedWord by remember { mutableStateOf<Word?>(null) }
    val isWordDetailVisible = uiState.selectedWord != null

    LaunchedEffect(uiState.selectedWord) {
        uiState.selectedWord?.let { displayedWord = it }
    }

    BackHandler(enabled = uiState.showWordAddPage) {
        onDismissAddWordPage()
    }

    BackHandler(enabled = isWordDetailVisible && !uiState.showWordAddPage) {
        onDismissWordDetail()
    }

    AnimatedContent(
        targetState = uiState.showWordAddPage,
        label = "WordAddPageTransition",
        transitionSpec = {
            if (targetState) {
                slideInHorizontally(MioMotion.emphasizedTween()) { it / 4 } +
                    fadeIn(MioMotion.standardTween()) togetherWith
                    slideOutHorizontally(MioMotion.standardTween()) { -it / 6 } +
                    fadeOut(MioMotion.standardTween())
            } else {
                slideInHorizontally(MioMotion.emphasizedTween()) { -it / 6 } +
                    fadeIn(MioMotion.standardTween()) togetherWith
                    slideOutHorizontally(MioMotion.standardTween()) { it / 4 } +
                    fadeOut(MioMotion.standardTween())
            }
        }
    ) { showWordAddPage ->
        if (showWordAddPage) {
            WordAddPage(
                onBack = onDismissAddWordPage,
                onWordAdded = onWordAdded
            )
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .blurIf(isWordDetailVisible)
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = wordBook.title,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = onBack) {
                                        Icon(
                                            painterResource(R.drawable.ic_arrow_back),
                                            contentDescription = "返回"
                                        )
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            LazyColumn(
                                contentPadding = innerPadding,
                                verticalArrangement = Arrangement.spacedBy(MioSpacing.xs),
                            ) {
                                items(
                                    items = uiState.words,
                                    key = { it.id }
                                ) { word ->
                                    WordEntryCard(
                                        word = word,
                                        onClick = { onWordClick(word) },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                            }
                            // 添加新单词 FAB
                            FloatingActionButton(
                                onClick = onAddWordClick,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(
                                        vertical = MioSize.fabBottomSpace,
                                        horizontal = MioSpacing.lg
                                    )
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_add),
                                    contentDescription = "添加新单词"
                                )
                            }
                        }
                    }
                }
                // 单词详情卡片
                AnimatedVisibility(
                    visible = isWordDetailVisible,
                    enter = fadeIn(MioMotion.standardTween()),
                    exit = fadeOut(MioMotion.exitTween())
                ) {
                    // 暗色遮罩
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onDismissWordDetail
                            )
                    )
                }

                AnimatedVisibility(
                    visible = isWordDetailVisible,
                    modifier = Modifier.align(Alignment.Center),
                    enter = fadeIn(MioMotion.standardTween()) + scaleIn(
                        animationSpec = MioMotion.emphasizedTween(),
                        initialScale = 0.96f
                    ),
                    exit = fadeOut(MioMotion.exitTween()) + scaleOut(
                        animationSpec = MioMotion.exitTween(),
                        targetScale = 0.98f
                    )
                ) {
                    // 放大卡片
                    displayedWord?.let { word ->
                        WordCardBase(
                            modifier = Modifier.tiltOnTouch(),
                            isShowingBack = true,
                            frontContent = {},
                            backContent = {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = word.kanji ?: word.kana,
                                        fontSize = 32.sp
                                    )
                                    if (word.kanji != null) {
                                        Spacer(modifier = Modifier.padding(MioSpacing.sm))
                                        Text(
                                            text = word.kana, fontSize = 20.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(MioSpacing.md))
                                    Text(
                                        text = word.romaji,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.padding(MioSpacing.md))
                                    Text(
                                        text = word.meaning,
                                        fontSize = 20.sp,
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
