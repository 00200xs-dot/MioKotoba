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
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.model.WordBook

@Composable
fun BookCard(
    wordBook: WordBook,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = wordBook.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.padding(3.dp))
            Text(
                text = wordBook.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.padding(3.dp))
            Text(
                text = "${wordBook.wordCount} 个单词",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.padding(2.dp))
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
                    .height(4.dp),
                color = ProgressIndicatorDefaults.linearColor,
                trackColor = ProgressIndicatorDefaults.linearTrackColor,
                strokeCap = StrokeCap.Round,
            )
        }
    }
}