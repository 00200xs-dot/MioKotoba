package com.akira.miokotoba.ui.features.study.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.ui.animation.AnimationUtils

@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    word: Word,
    //外部定义点击逻辑
    onCardClick: (() -> Unit)? = null,
    //外部控制翻转状态
    isFlipped: Boolean = false
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis =
                AnimationUtils.DURATION_MEDIUM,
            easing = AnimationUtils.standardEasing
        ),
        label = "CardFlipAnimation"
    )

    WordCardBase(
        modifier = modifier.graphicsLayer {
            rotationY = rotation
            cameraDistance = 12f * density
        },
        onClick = onCardClick,
        contentRotationY = if (rotation > 90f) 180f else 0f,
        isShowingBack = rotation > 90f,
        frontContent = { Text(word.kana, fontSize = 40.sp) },
        backContent = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 标题
                Text(
                    text = word.kanji ?: word.kana,
                    fontSize = 32.sp
                )
                // 有汉字显示假名行
                if (word.kanji != null) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = word.kana, fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                }
                Spacer(modifier = Modifier.padding(12.dp))
                // 罗马字
                Text(
                    text = word.romaji,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.padding(12.dp))
                // 中文释义
                Text(
                    text = word.meaning,
                    fontSize = 20.sp,
                )
            }
        }
    )
}