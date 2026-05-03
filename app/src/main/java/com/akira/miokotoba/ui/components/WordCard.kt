package com.akira.miokotoba.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    kana: String,
    kanji: String,
    translation: String,
    romaji: String,

    //是否支持边框参数
    borderStroke: BorderStroke? = null,
    //外部定义点击逻辑
    onCardClick: (() -> Unit)? = null,
    //外部控制翻转状态
    isFlipped: Boolean = false
) {
    // 根据是否提供边框参数，应用不同的修饰符
    val borderModifier = if (borderStroke != null) {
        Modifier.border(borderStroke, shape = RoundedCornerShape(45.dp))
    } else {
        Modifier
    }

    ElevatedCard(
        modifier = modifier
            .then(borderModifier)
            .fillMaxWidth(0.60f)
            .aspectRatio(0.6f),
        onClick = onCardClick ?: {},    //外部传入点击逻辑
        enabled = onCardClick != null,  //如果没有传入点击逻辑，则禁用点击
        shape = RoundedCornerShape(45.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            //指定禁用状态颜色与未禁用状态相同，保持视觉一致性
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = CardDefaults.elevatedCardElevation(
            pressedElevation = 12.dp,
            defaultElevation = 8.dp,
            hoveredElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = kana, fontSize = 40.sp, style = MaterialTheme.typography.headlineLarge
            )
            if (isFlipped) {
                Spacer(modifier = Modifier.padding(16.dp))
            } else {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = kanji,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = translation,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = romaji, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}