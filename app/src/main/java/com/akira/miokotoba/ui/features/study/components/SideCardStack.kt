package com.akira.miokotoba.ui.features.study.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.akira.miokotoba.ui.animation.AnimationUtils

/**
 * 侧边卡牌堆组件
 * 通过 isLeft 参数控制左右
 * cardWidth 和 peekWidth 控制尺寸和露出效果
 * scaleFactor 控制缩放，count 控制显示数量
 * 内部使用动画实现卡牌飞出效果
 * active 触发器确保新生成的卡牌有“从无到有”的变化
 */
@Composable
fun SideCardStack(
    modifier: Modifier = Modifier,
    isLeft: Boolean = true,
    cardWidth: Dp,
    peekWidth: Dp,
    scaleFactor: Float = 0.8f,
    count: Int = 0  //当前牌堆卡牌数量
) {
    //如果没有卡牌，直接不渲染任何东西
    if (count <= 0) return
    //限制最多显示3张
    val displayCount = count.coerceAtMost(3)

    val stackBorder = BorderStroke(
        width = 0.5.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    )
    //计算整体偏移，使得最后一张卡牌露出peekWidth
    val globalOffsetX = if (isLeft) {
        -(cardWidth - peekWidth)
    } else {
        cardWidth - peekWidth
    }

    Box(modifier = modifier.offset(x = globalOffsetX)) {
        for (i in 0 until displayCount) {
            //引入 active 触发器，确保新生成的卡片有“从无到有”的变化
            var active by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { active = true }

            val animatedAlpha by animateFloatAsState(
                targetValue = if (active) 1f else 0f,
                animationSpec = tween(AnimationUtils.DURATION_LONG),
                label = "cardAlpha"
            )

            val step = -i * 10.dp
            val animatedStep by animateFloatAsState(
                targetValue = if (active) step.value else 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "cardStep"
            )
            WordCard(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = animatedAlpha
                    }
                    .offset(
                        x = if (isLeft) animatedStep.dp else -animatedStep.dp
                    )
                    .width(cardWidth)
                    .scale(scaleFactor),
                kana = "", kanji = "", translation = "", romaji = "",
                borderStroke = stackBorder,
                isFlipped = false,
                onCardClick = null
            )
        }
    }
}