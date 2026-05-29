package com.akira.miokotoba.ui.features.wordbook.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.theme.MioDimens

@Composable
fun BookCard(
    wordBook: WordBook,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MioDimens.gapLg),
        shape = RoundedCornerShape(MioDimens.radiusXxl),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(MioDimens.gapXl),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = wordBook.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(MioDimens.gapXs))
            Text(
                text = wordBook.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(MioDimens.gapXs))
            Text(
                text = "${wordBook.wordCount} 个单词",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(MioDimens.gapXs))
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
                    .height(MioDimens.gapXs),
                color = ProgressIndicatorDefaults.linearColor,
                trackColor = ProgressIndicatorDefaults.linearTrackColor,
                strokeCap = StrokeCap.Round,
            )
        }
    }
}