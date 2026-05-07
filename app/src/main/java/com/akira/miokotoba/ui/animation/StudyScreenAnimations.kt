package com.akira.miokotoba.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * 封装卡片飞出的动画逻辑
 * 将动画与具体的业务 state 分离
 */
suspend fun swipeCardToSide(
    offsetX: Animatable<Float, *>,
    offsetY: Animatable<Float, *>,
    scale: Animatable<Float, *>,
    targetX: Float,
    targetY: Float,
    onComplete: () -> Unit
) {
    coroutineScope {
        // 1. 并行执行飞出并缩小动画
        // 这里可以使用 AnimationUtils.DURATION_MEDIUM 统一管理时间
        launch { offsetX.animateTo(targetX, tween(300)) }
        launch { offsetY.animateTo(targetY, tween(300)) }
        launch { scale.animateTo(0.8f, tween(300)) }
    }

    // 2. 执行业务回调 (如 count++)
    onComplete()

    // 3. 重置状态供下一张卡片使用
    offsetX.snapTo(0f)
    offsetY.snapTo(0f)
    scale.snapTo(1f)
}