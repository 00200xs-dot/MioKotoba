package com.akira.miokotoba.ui.features.study.components

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.animation.MioMotion
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.design.MioSize
import com.akira.miokotoba.ui.design.MioSpacing
import kotlin.math.roundToInt

@Composable
fun StudyBookPicker(
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
                    .padding(MioSpacing.lg)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {}
                    ),
                shape = RoundedCornerShape(MioRadius.xl),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 6.dp,
                shadowElevation = 10.dp
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(MioMotion.emphasizedTween()) { it / 4 } + fadeIn(
                        MioMotion.standardTween()
                    ),
                    exit = slideOutVertically(MioMotion.exitTween()) { it / 6 } +
                        fadeOut(MioMotion.exitTween())
                ) {
                    StudyBookPickerPanel(
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
private fun StudyBookPickerPanel(
    books: List<WordBook>,
    selectedBook: WordBook?,
    onPick: (WordBook) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.padding(MioSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(MioSpacing.md)
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
            verticalArrangement = Arrangement.spacedBy(MioSpacing.sm)
        ) {
            items(books, key = { it.id }) { book ->
                StudyBookPickerRow(
                    book = book,
                    selected = selectedBook?.id == book.id,
                    onClick = { onPick(book) }
                )
            }
        }
    }
}

@Composable
private fun StudyBookPickerRow(
    book: WordBook,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(MioRadius.lg),
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
                .padding(MioSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MioSpacing.md)
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

@Composable
fun StudyGlyph(
    icon: ImageVector,
    contentDescription: String
) {
    Surface(
        modifier = Modifier.size(MioSize.iconContainer),
        shape = RoundedCornerShape(MioRadius.lg),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(MioSize.iconMd)
            )
        }
    }
}