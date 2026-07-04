package com.akira.miokotoba.ui.features.wordbook.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.design.MioSpacing

@Composable
fun WordEntryCard(
    word: Word,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(MioRadius.lg),
    shadowElevation: Dp = 0.dp
) {
    Surface(
        modifier = modifier
            .padding(horizontal = MioSpacing.lg)
            .fillMaxWidth(),
        onClick = onClick,
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = shadowElevation
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = MioSpacing.md,
                vertical = MioSpacing.sm
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MioSpacing.xs)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WordToken(
                        kana = word.kana,
                        kanji = word.kanji
                    )

                    Spacer(modifier = Modifier.width(MioSpacing.md))

                    Text(
                        text = word.romaji,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "词",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(WordTokenWidth)
                    )

                    Spacer(modifier = Modifier.width(MioSpacing.md))

                    Text(
                        text = word.meaning,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(MioSpacing.sm))

            FamiliarityMark(
                mastered = word.mastered
            )
        }
    }
}

private val WordTokenWidth = 120.dp
private val WordTokenHeight = 44.dp

@Composable
private fun WordToken(
    kana: String,
    kanji: String?
) {
    val tokenColor = MaterialTheme.colorScheme.surfaceContainerHigh

    Surface(
        modifier = Modifier
            .width(WordTokenWidth)
            .height(WordTokenHeight),
        shape = RoundedCornerShape(MioRadius.lg),
        color = tokenColor
    ) {
        Box(
            modifier = Modifier.padding(horizontal = MioSpacing.sm),
            contentAlignment = Alignment.Center
        ) {
            if (kanji != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = kana,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                    Text(
                        text = kanji,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }
            } else {
                Text(
                    text = kana,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(MioSpacing.xxl)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color.Transparent, tokenColor)
                        )
                    )
            )
        }
    }
}

@Composable
private fun FamiliarityMark(
    mastered: Boolean
) {
    val color = if (mastered) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(color)
    )
}
