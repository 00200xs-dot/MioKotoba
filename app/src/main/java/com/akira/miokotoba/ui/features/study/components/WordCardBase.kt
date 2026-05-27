package com.akira.miokotoba.ui.features.study.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun WordCardBase(
    modifier: Modifier = Modifier,
    // 正反面显示
    isShowingBack: Boolean = false,
    // 边框参数
    borderStroke: BorderStroke? = null,
    onClick: (() -> Unit)? = null,
    // 内容反翻转角度（卡片翻转时文字保持正向）
    contentRotationY: Float = 0f,
    // 正面内容
    frontContent: @Composable () -> Unit,
    // 背面内容
    backContent: @Composable () -> Unit
) {
    val cardColor = MaterialTheme.colorScheme.surfaceContainerHigh

    Card(
        modifier = modifier
            // 如果有边框 应用边框参数
            .then(
                if (borderStroke != null) {
                    Modifier.border(borderStroke, RoundedCornerShape(45.dp))
                } else Modifier
            )
            .shadow(8.dp, RoundedCornerShape(45.dp))
            .fillMaxWidth(0.6f)
            .aspectRatio(0.6f),
        onClick = onClick ?: {},
        enabled = onClick != null,
        shape = RoundedCornerShape(45.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
    ) {
        if (isShowingBack) {
            BackLayout(
                contentRotationY = contentRotationY,
                content = backContent
            )
        } else {
            FrontLayout(
                contentRotationY = contentRotationY,
                content = frontContent
            )
        }
    }
}

// 卡片背面
@Composable
private fun BackLayout(
    contentRotationY: Float = 0f,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp, bottom = 12.dp)
    ) {
        // 顶部装饰细线
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .clip(RoundedCornerShape(topStart = 45.dp, topEnd = 45.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        )
        // 内容
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(24.dp)
                .graphicsLayer { rotationY = contentRotationY },
            contentAlignment = Alignment.Center
        ) {
            content()
        }
        // 底部装饰细线
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .clip(RoundedCornerShape(bottomStart = 45.dp, bottomEnd = 45.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        )
    }
}

// 卡片正面
@Composable
private fun FrontLayout(
    contentRotationY: Float = 0f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .graphicsLayer { rotationY = contentRotationY },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}