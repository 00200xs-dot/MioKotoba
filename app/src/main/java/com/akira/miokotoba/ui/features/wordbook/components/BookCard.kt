package com.akira.miokotoba.ui.features.wordbook.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.components.MioSurfaceCard
import com.akira.miokotoba.ui.design.MioSpacing

@Composable
fun BookCard(
    wordBook: WordBook,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MioSurfaceCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MioSpacing.lg),
        onClick = onClick
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = wordBook.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(MioSpacing.xs))
            Text(
                text = wordBook.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(MioSpacing.xs))
            Text(
                text = "${wordBook.wordCount} 个单词",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(MioSpacing.xs))
            //进度条
            LinearProgressIndicator(
                progress = {
                    if (wordBook.wordCount > 0) {
                        wordBook.learnedCount.toFloat() / wordBook.wordCount.toFloat()
                    } else {
                        0f
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MioSpacing.xs),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round,
            )
        }
    }
}
