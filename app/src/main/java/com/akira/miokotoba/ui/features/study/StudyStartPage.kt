package com.akira.miokotoba.ui.features.study

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.akira.miokotoba.AppContainer
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.animation.MioMotion
import com.akira.miokotoba.ui.navigation.Screen
import com.akira.miokotoba.ui.theme.MioDimens
import kotlin.math.roundToInt

@Composable
fun StudyStartPage(
    navController: NavController
) {
    val wordBookRepository = AppContainer.wordBookRepository
    val books = wordBookRepository.getBooks()
    var selectedBook by remember { mutableStateOf(books.firstOrNull()) }
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

        BookPickerOverlay(
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
            .padding(horizontal = MioDimens.gapLg),
        verticalArrangement = Arrangement.spacedBy(MioDimens.gapLg)
    ) {
        CurrentBookCard(
            book = selectedBook,
            onClick = onPickBook
        )

        StudyProgressSummary(book = selectedBook)

        QuickStudyLinks(onKanaChart = onKanaChart)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onStart,
            enabled = selectedBook != null,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = MioDimens.gapLg)
                .height(58.dp),
            shape = RoundedCornerShape(MioDimens.radiusLg),
            contentPadding = PaddingValues(horizontal = MioDimens.gapXl),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(MioDimens.gapSm))
            Text(
                text = "开始学习",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CurrentBookCard(
    book: WordBook?,
    onClick: () -> Unit
) {
    val progress = book?.progress() ?: 0f

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = MioDimens.gapSm),
        shape = RoundedCornerShape(MioDimens.radiusXxl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(MioDimens.gapXl),
            verticalArrangement = Arrangement.spacedBy(MioDimens.gapMd)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MioDimens.gapMd)
            ) {
                StudyGlyph(
                    icon = Icons.Rounded.AutoStories,
                    contentDescription = "当前词书"
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = book?.title ?: "选择一本词书",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = book?.description?.ifBlank { "准备开始今天的学习" } ?: "先选择一本词书开始学习",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    imageVector = Icons.Rounded.ExpandMore,
                    contentDescription = "切换词书",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BookMeta("${book?.wordCount ?: 0} 词", "总词数")
                BookMeta("${book?.learnedCount ?: 0} 词", "已学")
                BookMeta("${(progress * 100).roundToInt()}%", "进度")
            }
        }
    }
}

@Composable
private fun StudyProgressSummary(book: WordBook?) {
    val remaining = ((book?.wordCount ?: 0) - (book?.learnedCount ?: 0)).coerceAtLeast(0)
    val newWords = remaining.coerceAtMost(10)
    val reviewWords = (book?.learnedCount ?: 0).coerceAtMost(8)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MioDimens.gapMd)
    ) {
        SummaryTile(
            label = "待复习",
            value = reviewWords.toString(),
            icon = Icons.Rounded.Schedule,
            modifier = Modifier.weight(1f)
        )
        SummaryTile(
            label = "新词",
            value = newWords.toString(),
            icon = Icons.Rounded.AutoStories,
            modifier = Modifier.weight(1f)
        )
        SummaryTile(
            label = "已掌握",
            value = (book?.learnedCount ?: 0).toString(),
            icon = Icons.Rounded.CheckCircle,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickStudyLinks(onKanaChart: () -> Unit) {
    OutlinedButton(
        onClick = onKanaChart,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(MioDimens.radiusLg),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Icon(
            imageVector = Icons.Rounded.GridView,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(MioDimens.gapSm))
        Text("五十音图")
    }
}

@Composable
private fun SummaryTile(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.heightIn(min = 96.dp),
        shape = RoundedCornerShape(MioDimens.radiusLg),
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(MioDimens.gapMd),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BookMeta(value: String, label: String) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StudyGlyph(
    icon: ImageVector,
    contentDescription: String
) {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(MioDimens.radiusLg),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun BookPickerOverlay(
    visible: Boolean,
    books: List<WordBook>,
    selectedBook: WordBook?,
    onDismiss: () -> Unit,
    onPick: (WordBook) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(MioMotion.standardTween()),
        exit = fadeOut(MioMotion.exitTween())
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDismiss
                )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(MioDimens.gapLg)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {}
                    ),
                shape = RoundedCornerShape(MioDimens.radiusXxl),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 6.dp,
                shadowElevation = 10.dp
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(MioMotion.emphasizedTween()) { it / 4 } + fadeIn(MioMotion.standardTween()),
                    exit = slideOutVertically(MioMotion.exitTween()) { it / 6 } + fadeOut(MioMotion.exitTween())
                ) {
                    BookPickerPanel(
                        books = books,
                        selectedBook = selectedBook,
                        onPick = onPick,
                        onDismiss = onDismiss
                    )
                }
            }
        }
    }
}

@Composable
private fun BookPickerPanel(
    books: List<WordBook>,
    selectedBook: WordBook?,
    onPick: (WordBook) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.padding(MioDimens.gapLg),
        verticalArrangement = Arrangement.spacedBy(MioDimens.gapMd)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "选择词书",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Rounded.ExpandMore,
                    contentDescription = "收起"
                )
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        LazyColumn(
            modifier = Modifier.heightIn(max = 420.dp),
            verticalArrangement = Arrangement.spacedBy(MioDimens.gapSm)
        ) {
            items(books, key = { it.id }) { book ->
                BookPickerRow(
                    book = book,
                    selected = selectedBook?.id == book.id,
                    onClick = { onPick(book) }
                )
            }
        }
    }
}

@Composable
private fun BookPickerRow(
    book: WordBook,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(MioDimens.radiusLg),
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MioDimens.gapMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MioDimens.gapMd)
        ) {
            StudyGlyph(
                icon = Icons.Rounded.AutoStories,
                contentDescription = book.title
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                )
                Text(
                    text = "${book.wordCount} 词 · 已学 ${book.learnedCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "${(book.progress() * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alpha(if (selected) 1f else 0.72f)
            )
        }
    }
}

private fun WordBook.progress(): Float {
    if (wordCount <= 0) return 0f
    return learnedCount.toFloat() / wordCount.toFloat()
}
