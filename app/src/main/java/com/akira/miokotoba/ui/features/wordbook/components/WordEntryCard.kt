package com.akira.miokotoba.ui.features.wordbook.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.ui.components.MioListItem
import com.akira.miokotoba.ui.design.MioSpacing

@Composable
fun WordEntryCard(
    word: Word,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MioListItem(
        modifier = modifier
            .padding(horizontal = MioSpacing.lg),
        title = word.kanji ?: word.kana,
        subtitle = buildString {
            append(if (word.kanji != null) "${word.kana}  ${word.romaji}" else word.romaji)
            append('\n')
            append(word.meaning)
        },
        onClick = onClick,
        trailing = {
            if (word.mastered) {
                Text(
                    text = "已掌握",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}
