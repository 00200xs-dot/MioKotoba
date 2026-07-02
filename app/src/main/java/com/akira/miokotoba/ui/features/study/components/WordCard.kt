package com.akira.miokotoba.ui.features.study.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.ui.design.MioMotion
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.design.MioSpacing

@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    word: Word,
    onCardClick: (() -> Unit)? = null,
    isFlipped: Boolean = false
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = MioMotion.standardTween(),
        label = "CardFlipAnimation"
    )

    WordCardBase(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
        onClick = onCardClick,
        contentRotationY = if (rotation > 90f) 180f else 0f,
        isShowingBack = rotation > 90f,
        frontContent = { Text(word.kana, fontSize = 40.sp) },
        backContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(MioSpacing.sm),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = word.kanji ?: word.kana,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.padding(MioSpacing.md))
                if (word.kanji != null) {
                    InfoChip("読み", word.kana, word.romaji)
                } else {
                    InfoChip("Roman", word.romaji)
                }
                InfoChip("意味", word.meaning)
            }
        }
    )
}

@Composable
private fun InfoChip(
    label: String,
    primary: String,
    secondary: String? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MioSpacing.xs),
        shape = RoundedCornerShape(MioRadius.sm),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = MioSpacing.lg, vertical = MioSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MioSpacing.xs)
                ) {
                    Text(
                        text = primary,
                        fontSize = 18.sp
                    )
                    if (secondary != null) {
                        Text(
                            text = secondary,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
