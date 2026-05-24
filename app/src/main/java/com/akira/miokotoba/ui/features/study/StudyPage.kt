package com.akira.miokotoba.ui.features.study

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.ui.features.study.animations.swipeCardToSide
import com.akira.miokotoba.ui.features.study.components.SideCardStack
import com.akira.miokotoba.ui.features.study.components.WordCard
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun StudyPage() {
    // 1. 状态声明
    var isCenterFlipped by rememberSaveable { mutableStateOf(false) }
    var leftCount by rememberSaveable { mutableIntStateOf(0) }
    var rightCount by rememberSaveable { mutableIntStateOf(0) }

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val peekWidth = 40.dp
        val cardWidth = maxWidth * 0.6f
        val screenWidth = constraints.maxWidth.toFloat()
        val screenHeight = constraints.maxHeight.toFloat()

        Box(modifier = Modifier.fillMaxSize()) {

            // 左侧牌堆
            SideCardStack(
                modifier = Modifier.align(Alignment.CenterStart),
                isLeft = true,
                cardWidth = cardWidth,
                peekWidth = peekWidth,
                count = leftCount
            )

            // 中间主卡片
            WordCard(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                    .scale(scale.value)
                    .graphicsLayer { rotationZ = (offsetX.value / screenWidth) * 15f }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                scope.launch {
                                    offsetX.snapTo(offsetX.value + dragAmount.x)
                                    offsetY.snapTo(offsetY.value + dragAmount.y)
                                    scale.snapTo(0.95f)
                                }
                            },
                            onDragEnd = {
                                scope.launch {
                                    val x = offsetX.value
                                    val y = offsetY.value
                                    // 根据方向调用封装好的动画函数
                                    when {
                                        x > 300f -> swipeCardToSide(offsetX, offsetY, scale, screenWidth, y) { rightCount++ }
                                        x < -300f -> swipeCardToSide(offsetX, offsetY, scale, -screenWidth, y) { leftCount++ }
                                        y > 400f -> swipeCardToSide(offsetX, offsetY, scale, x, screenHeight) { /* 模糊逻辑 */ }
                                        y < -400f -> swipeCardToSide(offsetX, offsetY, scale, x, -screenHeight) { /* 简单逻辑 */ }
                                        else -> {
                                            launch { offsetX.animateTo(0f, spring(Spring.DampingRatioLowBouncy)) }
                                            launch { offsetY.animateTo(0f, spring(Spring.DampingRatioLowBouncy)) }
                                            launch { scale.animateTo(1f, spring()) }
                                        }
                                    }
                                }
                            }
                        )
                    },
                kana = "あ", kanji = "亜", translation = "Asia", romaji = "a",
                isFlipped = isCenterFlipped,
                onCardClick = { isCenterFlipped = !isCenterFlipped }
            )

            // 右侧牌堆
            SideCardStack(
                modifier = Modifier.align(Alignment.CenterEnd),
                isLeft = false,
                cardWidth = cardWidth,
                peekWidth = peekWidth,
                count = rightCount
            )
        }
    }
}