package com.akira.miokotoba.ui.features.study

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.ui.features.study.components.WordCard
import com.akira.miokotoba.ui.modifier.tiltOnTouch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun StudyPage() {
    var isCenterFlipped by rememberSaveable { mutableStateOf(false) }
    var reviewedCount by rememberSaveable { mutableIntStateOf(0) }

    val ratingButtonY = remember { Animatable(300f) }
    var showRatingButtons by rememberSaveable { mutableStateOf(false) }
    val surfaceWidth = remember { Animatable(48f) }
    val surfaceAlpha = remember { Animatable(0f) }

    val offsetY = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val tiltZ = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    // 测试数据
    val testWords = remember {
        listOf(
            Word("1", "あ", "亜", "a", "Asia", false),
            Word("2", "い", "伊", "i", "Italy", false),
            Word("3", "う", "宇", "u", "space", false),
            Word("4", "え", "江", "e", "bay", false),
            Word("5", "お", "於", "o", "おわり", false),
        )
    }
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentWord = testWords[currentIndex]


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val screenHeight = constraints.maxHeight.toFloat()

        LaunchedEffect(currentIndex) {
            // 退场效果残留状态恢复
            scale.snapTo(1f)
            tiltZ.snapTo(0f)
            surfaceAlpha.snapTo(0f)
            // 入场效果
            offsetY.snapTo(-screenHeight)
            offsetY.animateTo(
                0f,
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            WordCard(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset { IntOffset(0, offsetY.value.roundToInt()) }
                    .scale(scale.value)
                    .tiltOnTouch()
                    .graphicsLayer { rotationZ = tiltZ.value },
                word = currentWord,
                isFlipped = isCenterFlipped,
                onCardClick = {
                    isCenterFlipped = !isCenterFlipped
                    if (isCenterFlipped) {
                        showRatingButtons = false
                        scope.launch {
                            surfaceWidth.snapTo(48f)
                            launch { surfaceAlpha.animateTo(1f, tween(200)) }
                            launch { ratingButtonY.animateTo(0f, spring()) }
                            launch { surfaceWidth.animateTo(320f, spring()) }
                            showRatingButtons = true
                        }
                    } else {
                        scope.launch {
                            showRatingButtons = false
                            launch { surfaceAlpha.animateTo(0f, tween(200)) }
                            launch { surfaceWidth.animateTo(48f, spring()) }
                            launch { ratingButtonY.animateTo(300f, spring()) }
                        }
                    }
                }
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .offset { IntOffset(0, ratingButtonY.value.roundToInt()) }
                    .width(surfaceWidth.value.roundToInt().dp)
                    .graphicsLayer { alpha = surfaceAlpha.value },
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 0.dp,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Box(
                    modifier = Modifier.height(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = showRatingButtons,
                        transitionSpec = {
                            fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                        }
                    ) { visible ->
                        if (!visible) {
                            Text("")
                        } else {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                RatingChip(
                                    label = "不认识",
                                    onClick = {
                                        scope.launch {
                                            showRatingButtons = false
                                            launch { surfaceAlpha.animateTo(0f, tween(200)) }
                                            launch { surfaceWidth.animateTo(48f, spring()) }
                                            launch { ratingButtonY.animateTo(300f, spring()) }
                                            exitCard(
                                                offsetY, tiltZ, scale, screenHeight,
                                                onComplete = {
                                                    currentIndex =
                                                        (currentIndex + 1) % testWords.size
                                                    isCenterFlipped = false
                                                    reviewedCount++
                                                }
                                            )
                                        }
                                    }
                                )
                                RatingChip(
                                    label = "模糊",
                                    onClick = {
                                        scope.launch {
                                            showRatingButtons = false
                                            launch { surfaceAlpha.animateTo(0f, tween(200)) }
                                            launch { surfaceWidth.animateTo(48f, spring()) }
                                            launch { ratingButtonY.animateTo(300f, spring()) }
                                            exitCard(
                                                offsetY, tiltZ, scale, screenHeight,
                                                onComplete = {
                                                    currentIndex =
                                                        (currentIndex + 1) % testWords.size
                                                    isCenterFlipped = false
                                                    reviewedCount++
                                                }
                                            )
                                        }
                                    }
                                )
                                RatingChip(
                                    label = "认识",
                                    onClick = {
                                        scope.launch {
                                            showRatingButtons = false
                                            launch { surfaceAlpha.animateTo(0f, tween(200)) }
                                            launch { surfaceWidth.animateTo(48f, spring()) }
                                            launch { ratingButtonY.animateTo(300f, spring()) }
                                            exitCard(
                                                offsetY, tiltZ, scale, screenHeight,
                                                onComplete = {
                                                    currentIndex =
                                                        (currentIndex + 1) % testWords.size
                                                    isCenterFlipped = false
                                                    reviewedCount++
                                                }
                                            )
                                        }
                                    }
                                )
                                RatingChip(
                                    label = "简单",
                                    onClick = {
                                        scope.launch {
                                            showRatingButtons = false
                                            launch { surfaceAlpha.animateTo(0f, tween(200)) }
                                            launch { surfaceWidth.animateTo(48f, spring()) }
                                            launch { ratingButtonY.animateTo(300f, spring()) }
                                            exitCard(
                                                offsetY, tiltZ, scale, screenHeight,
                                                onComplete = {
                                                    currentIndex =
                                                        (currentIndex + 1) % testWords.size
                                                    isCenterFlipped = false
                                                    reviewedCount++
                                                }
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 退场效果
private suspend fun exitCard(
    offsetY: Animatable<Float, *>,
    tiltZ: Animatable<Float, *>,
    scale: Animatable<Float, *>,
    targetY: Float,
    onComplete: () -> Unit
) {
    // 随机 ±5°
    val randomAngle = Random.nextFloat() * 10f - 5f
    coroutineScope {
        launch { offsetY.animateTo(targetY, tween(300)) }
        launch { tiltZ.animateTo(randomAngle, tween(300)) }
        launch { scale.animateTo(0.8f, tween(300)) }
    }
    onComplete()
}

@Composable
private fun RatingChip(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.widthIn(min = 72.dp),
        onClick = onClick,
        shape = RoundedCornerShape(45.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}