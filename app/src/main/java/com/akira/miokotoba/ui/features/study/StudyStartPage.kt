package com.akira.miokotoba.ui.features.study

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.akira.miokotoba.AppContainer
import com.akira.miokotoba.model.SampleData
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.animation.AnimationUtils
import com.akira.miokotoba.ui.navigation.Screen
import com.akira.miokotoba.ui.theme.MioDimens

@Composable
fun StudyStartPage(
    navController: NavController
) {
    val wordBookRepository = AppContainer.wordBookRepository
    val books =wordBookRepository.getBooks()
    var selectedBook by remember { mutableStateOf(books.firstOrNull()) }

    InitialContent(
        selectedBook = selectedBook,
        books = books,
        onBookSelected = { selectedBook = it },
        onStart = {
            selectedBook?.let { book ->
                navController.navigate(Screen.StudySession.createRoute(book.id))
            }
        },
        onKanaChart = { navController.navigate(Screen.KanaChart.route) }
    )
}

/**
 * 初始状态：选词书 + 开始按钮 + 五十音入口
 */
@Composable
private fun InitialContent(
    selectedBook: WordBook?,
    books: List<WordBook>,
    onBookSelected: (WordBook) -> Unit,
    onStart: () -> Unit,
    onKanaChart: () -> Unit,
) {
    var showPicker by remember { mutableStateOf(false) }

    SharedTransitionLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(MioDimens.gapLg)
        ) {
            // 正常页面内容层
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 词书 Chip 区域 — Box 固定高度，picker 展开时不会塌缩
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(72.dp)
                        .padding(top = 36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = !showPicker,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        BookChip(
                            label = selectedBook?.title ?: "暂未选择词书",
                            subtitle = selectedBook?.description?.ifEmpty { "请选择一本词书开始学习" }
                                ?: "请选择一本词书开始学习",
                            onClick = { showPicker = true },
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedVisibility
                        )
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(0.9f),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        StatCard(
                            label = "总词数",
                            value = selectedBook?.wordCount?.toString() ?: "-",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "已学",
                            value = selectedBook?.learnedCount?.toString() ?: "-",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.BottomCenter),
                        shape = RoundedCornerShape(64.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shadowElevation = MioDimens.cardShadow,
                        onClick = onStart
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "开始",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onKanaChart) {
                        Text("五十音图")
                    }
                }
            }

            // 词书选择器覆盖层
            AnimatedVisibility(visible = showPicker) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { showPicker = false }
                ) {
                    BookPickerPanel(
                        books = books,
                        selectedBook = selectedBook,
                        onPick = { book ->
                            onBookSelected(book)
                            showPicker = false
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility
                    )
                }
            }
        }
    }
}

/**
 * 词书列表面板：容器转换的展开状态
 */
@Composable
private fun BookPickerPanel(
    books: List<WordBook>,
    selectedBook: WordBook?,
    onPick: (WordBook) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Surface(
            modifier = Modifier
                .sharedBounds(
                    rememberSharedContentState("pickerBounds"),
                    animatedVisibilityScope,
                    enter = fadeIn(tween(AnimationUtils.DURATION_MEDIUM)),
                    exit = fadeOut(tween(AnimationUtils.DURATION_MEDIUM)),
                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds()
                )
                .fillMaxSize()
                .padding(MioDimens.gapXl),
            shape = RoundedCornerShape(MioDimens.radiusXxl),
            color = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Column(
                modifier = Modifier.padding(MioDimens.gapMd),
                verticalArrangement = Arrangement.spacedBy(MioDimens.gapMd)
            ) {
                Text(
                    "选择词书",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = MioDimens.gapSm)
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.secondaryFixedDim)
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(MioDimens.gapSm)
                ) {
                    items(books, key = { it.id }) { book ->
                        val isSelected = book.id == selectedBook?.id
                        Card(
                            onClick = { onPick(book) },
                            shape = RoundedCornerShape(MioDimens.radiusLg),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceDim
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(MioDimens.gapLg),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    book.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                Spacer(Modifier.height(MioDimens.gapXs))
                                Text(
                                    book.description.ifEmpty { "${book.wordCount} 词 · 已学 ${book.learnedCount}" },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(MioDimens.gapXs))
                                LinearProgressIndicator(
                                    progress = {
                                        if (book.wordCount > 0)
                                            book.learnedCount.toFloat() / book.wordCount.toFloat()
                                        else 0f
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp),
                                    color = if (isSelected)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    strokeCap = StrokeCap.Round
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 词书选择器入口：点击后容器转换到词书列表
 */
@Composable
private fun BookChip(
    label: String,
    subtitle: String,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val shape = RoundedCornerShape(MioDimens.radiusXxl)
    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .sharedBounds(
                    rememberSharedContentState("pickerBounds"),
                    animatedVisibilityScope,
                    enter = fadeIn(tween(AnimationUtils.DURATION_MEDIUM)),
                    exit = fadeOut(tween(AnimationUtils.DURATION_MEDIUM)),
                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds()
                )
                .fillMaxWidth(0.8f)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh, shape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = MioDimens.gapXl,
                    vertical = MioDimens.gapMd
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(MioDimens.gapXs))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text("▼", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * 统计小卡片：标签 + 数值，居中排列
 */
@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(horizontal = MioDimens.gapSm),
        shape = RoundedCornerShape(MioDimens.radiusLg),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MioDimens.gapXl, vertical = MioDimens.gapMd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}