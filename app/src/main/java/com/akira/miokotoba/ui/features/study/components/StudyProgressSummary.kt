package com.akira.miokotoba.ui.features.study.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.design.MioSpacing

@Composable
fun StudyProgressSummary(book: WordBook?) {
    val remaining = ((book?.wordCount ?: 0) - (book?.learnedCount ?: 0)).coerceAtLeast(0)
    val newWords = remaining.coerceAtMost(10)
    val reviewWords = (book?.learnedCount ?: 0).coerceAtMost(8)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MioSpacing.md)
    ) {
        StudyStartTile(
            label = "待复习",
            value = reviewWords.toString(),
            icon = Icons.Rounded.Schedule,
            modifier = Modifier.weight(1f)
        )
        StudyStartTile(
            label = "新词",
            value = newWords.toString(),
            icon = Icons.Rounded.AutoStories,
            modifier = Modifier.weight(1f)
        )
        StudyStartTile(
            label = "已掌握",
            value = (book?.learnedCount ?: 0).toString(),
            icon = Icons.Rounded.CheckCircle,
            modifier = Modifier.weight(1f)
        )
    }
}