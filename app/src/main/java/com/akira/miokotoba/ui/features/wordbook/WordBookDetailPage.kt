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
import androidx.compose.animation.core.animateDpAsState
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.R
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.ui.design.MioMotion
import com.akira.miokotoba.ui.design.MioSize
import com.akira.miokotoba.ui.design.MioSpacing
import com.akira.miokotoba.ui.components.topbar.MioTopBar
import com.akira.miokotoba.ui.components.topbar.MioTopBarAction
import com.akira.miokotoba.ui.components.topbar.MioTopBarActionStyle
import com.akira.miokotoba.ui.components.topbar.MioTopBarActionType
import com.akira.miokotoba.ui.components.topbar.MioTopBarDensity
import com.akira.miokotoba.ui.components.topbar.MioTopBarNavigation
import com.akira.miokotoba.ui.components.topbar.MioTopBarSearchState
import com.akira.miokotoba.ui.components.topbar.MioTopBarState
import com.akira.miokotoba.ui.features.study.components.WordCardBase
import com.akira.miokotoba.ui.features.wordbook.components.SwipeWordEntryItem
import com.akira.miokotoba.ui.modifier.blurIf
import com.akira.miokotoba.ui.modifier.tiltOnTouch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun WordBookDetailPage(
    bookId: String,
    uiState: WordBookDetailUiState,
    onAddWordClick: () -> Unit,
    onDismissAddWordPage: () -> Unit,
    onWordAdded: (Word) -> Unit,
    onDismissWordEditPage: () -> Unit,
    onWordUpdated: (Word) -> Unit,
    onEditWordClick: (Word) -> Unit,
    onDeleteWordClick: (Word) -> Unit,
    onDismissDeleteDialog: () -> Unit,
    onConfirmDeleteWord: () -> Unit,
    onWordClick: (Word) -> Unit,
    onDismissWordDetail: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onBack: () -> Unit
) {
    if (uiState.bookId != bookId) return

    val wordBook = uiState.wordBook ?: return
    var displayedWord by remember { mutableStateOf<Word?>(null) }
    val wordBeingEdited = uiState.editingWord
    val isWordFormVisible = uiState.showWordAddPage || wordBeingEdited != null
    val isWordDetailVisible = uiState.selectedWord != null
    var isSearchActive by rememberSaveable(bookId) { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var isFabVisible by remember { mutableStateOf(true) }
    val topBarState = MioTopBarState(
        title = wordBook.title,
        navigationIcon = MioTopBarNavigation.Back,
        actions = listOf(
            MioTopBarAction(
                iconRes = R.drawable.ic_topbar_search,
                contentDescription = "搜索单词",
                type = MioTopBarActionType.Search,
                style = MioTopBarActionStyle.Filled
            )
        ),
        searchState = MioTopBarSearchState(
            query = uiState.searchQuery,
            active = isSearchActive
        ),
        density = MioTopBarDensity.Compact
    )
    val fabOffsetX by animateDpAsState(
        targetValue = if (isFabVisible) 0.dp else MioSize.iconContainer + MioSpacing.xxl,
        animationSpec = MioMotion.standardTween(),
        label = "WordBookDetailFabOffset"
    )

    LaunchedEffect(uiState.selectedWord) {
        uiState.selectedWord?.let { displayedWord = it }
    }

    LaunchedEffect(listState) {
        var previousIndex = listState.firstVisibleItemIndex
        var previousOffset = listState.firstVisibleItemScrollOffset

        snapshotFlow {
            Triple(
                listState.isScrollInProgress,
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset
            )
        }
            .distinctUntilChanged()
            .collectLatest { (isScrolling, index, offset) ->
                val hasActualScroll = index != previousIndex || offset != previousOffset

                if (!isScrolling) {
                    isFabVisible = true
                } else if (hasActualScroll) {
                    isFabVisible = false
                }

                previousIndex = index
                previousOffset = offset
            }
    }

    BackHandler(enabled = isWordFormVisible) {
        if (wordBeingEdited != null) {
            onDismissWordEditPage()
        } else {
            onDismissAddWordPage()
        }
    }

    BackHandler(enabled = isWordDetailVisible && !isWordFormVisible) {
        onDismissWordDetail()
    }

    BackHandler(enabled = isSearchActive && !isWordFormVisible && !isWordDetailVisible) {
        isSearchActive = false
        onSearchQueryChange("")
    }

    AnimatedContent(
        targetState = isWordFormVisible,
        label = "WordFormPageTransition",
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
    ) { showWordForm ->
        if (showWordForm) {
            WordAddPage(
                initialWord = wordBeingEdited,
                onBack = {
                    if (wordBeingEdited != null) {
                        onDismissWordEditPage()
                    } else {
                        onDismissAddWordPage()
                    }
                },
                onSubmit = { word ->
                    if (wordBeingEdited != null) {
                        onWordUpdated(word)
                    } else {
                        onWordAdded(word)
                    }
                }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .blurIf(isWordDetailVisible)
                ) {
                    uiState.wordPendingDelete?.let { word ->
                        AlertDialog(
                            onDismissRequest = onDismissDeleteDialog,
                            title = {
                                Text("删除单词？")
                            },
                            text = {
                                Text("确定要删除「${word.kanji ?: word.kana}」吗？这个操作无法撤销。")
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = onConfirmDeleteWord
                                ) {
                                    Text(
                                        text = "删除",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = onDismissDeleteDialog
                                ) {
                                    Text("取消")
                                }
                            }
                        )
                    }

                    Scaffold(
                        topBar = {
                            MioTopBar(
                                state = topBarState,
                                onNavigationClick = onBack,
                                onActionClick = { actionType ->
                                    when (actionType) {
                                        MioTopBarActionType.Search -> isSearchActive = true
                                        MioTopBarActionType.Settings -> Unit
                                        MioTopBarActionType.More -> Unit
                                    }
                                },
                                onSearchQueryChange = onSearchQueryChange,
                                onSearchDismiss = {
                                    isSearchActive = false
                                    onSearchQueryChange("")
                                }
                            )
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                state = listState,
                                contentPadding = PaddingValues(
                                    start = MioSpacing.lg,
                                    top = innerPadding.calculateTopPadding() + MioSpacing.md,
                                    end = MioSpacing.lg,
                                    bottom = MioSize.fabBottomSpace + MioSpacing.xxxl
                                ),
                                verticalArrangement = Arrangement.spacedBy(MioSpacing.xs),
                            ) {
                                items(
                                    items = uiState.filteredWords,
                                    key = { it.id }
                                ) { word ->
                                    SwipeWordEntryItem(
                                        word = word,
                                        onClick = { onWordClick(word) },
                                        onEdit = { onEditWordClick(word) },
                                        onDelete = { onDeleteWordClick(word) },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                            }
                            // 添加新单词 FAB
                            FloatingActionButton(
                                onClick = onAddWordClick,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = fabOffsetX)
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
