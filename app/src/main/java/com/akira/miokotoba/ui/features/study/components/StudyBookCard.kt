package com.akira.miokotoba.ui.features.study.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.components.MioSurfaceCard
import com.akira.miokotoba.ui.design.MioSpacing
import kotlin.math.roundToInt

@Composable
fun StudyBookCard(
    book: WordBook?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (book == null || book.wordCount == 0) {
        0f
    } else {
        book.learnedCount.toFloat() / book.wordCount
    }

    MioSurfaceCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MioSpacing.lg)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MioSpacing.md)
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
                        text = book?.description?.ifBlank { "准备开始今天的学习" }
                            ?: "先选择一本词书开始学习",
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
