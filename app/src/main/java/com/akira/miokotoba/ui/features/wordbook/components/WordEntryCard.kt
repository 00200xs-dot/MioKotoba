package com.akira.miokotoba.ui.features.wordbook.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.model.Word

@Composable
fun WordEntryCard(
    word: Word,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = word.kanji ?: word.kana,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                if (word.mastered) {
                    Text(
                        text = "已掌握",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                text = if (word.kanji != null) "${word.kana}  ${word.romaji}" else word.romaji,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = word.meaning,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun test() {
    WordEntryCard(
        Word(
            id = "111",
            kanji = "漢字",
            kana = "かんじ",
            romaji = "kanji",
            meaning = "汉字",
            mastered = true
        ),
        onClick = {}
    )

}