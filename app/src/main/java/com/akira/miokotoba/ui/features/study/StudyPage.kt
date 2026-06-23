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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.R
import com.akira.miokotoba.model.SampleData
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.ui.animation.AnimationUtils
import com.akira.miokotoba.ui.features.study.components.WordCard
import com.akira.miokotoba.ui.modifier.tiltOnTouch
import com.akira.miokotoba.ui.theme.MioDimens
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun StudyPage(
    words: List<Word> = SampleData.wordsForBook("1")
) {
    if (words.isEmpty()) {
        EmptyStudyContent()
        return
    }

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
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentWord = words[currentIndex.coerceIn(words.indices)]


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(MioDimens.gapLg)
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
                            launch {
                                surfaceAlpha.animateTo(
                                    1f,
                                    tween(AnimationUtils.DURATION_SHORT)
                                )
                            }
                            launch { ratingButtonY.animateTo(0f, spring()) }
                            launch { surfaceWidth.animateTo(320f, spring()) }
                            showRatingButtons = true
                        }
                    } else {
                        scope.launch {
                            showRatingButtons = false
                            launch {
                                surfaceAlpha.animateTo(
                                    0f,
                                    tween(AnimationUtils.DURATION_SHORT)
                                )
                            }
                            launch { surfaceWidth.animateTo(48f, spring()) }
                            launch { ratingButtonY.animateTo(300f, spring()) }
                        }
                    }
                }
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = MioDimens.gapLg)
                    .offset { IntOffset(0, ratingButtonY.value.roundToInt()) }
                    .width(surfaceWidth.value.roundToInt().dp)
                    .graphicsLayer { alpha = surfaceAlpha.value },
                shape = RoundedCornerShape(MioDimens.radiusLg),
                shadowElevation = 0.dp,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Box(
                    modifier = Modifier
                        .padding(MioDimens.gapSm)
                        .heightIn(min = 52.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = showRatingButtons,
                        transitionSpec = {
                            fadeIn(
                                tween(
                                    AnimationUtils.DURATION_SHORT
                                )
                            ) togetherWith fadeOut(
                                tween(
                                    AnimationUtils.DURATION_SHORT
                                )
                            )
                        }
                    ) { visible ->
                        if (!visible) {
                            Text("")
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(MioDimens.gapSm),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RatingChip(
                                    label = "不认识",
                                    iconRes = R.drawable.ic_level_not_know,
                                    onClick = {
                                        scope.launch {
                                            showRatingButtons = false
                                            launch {
                                                surfaceAlpha.animateTo(
                                                    0f,
                                                    tween(AnimationUtils.DURATION_SHORT)
                                                )
                                            }
                                            launch { surfaceWidth.animateTo(48f, spring()) }
                                            launch { ratingButtonY.animateTo(300f, spring()) }
                                            exitCard(
                                                offsetY, tiltZ, scale, screenHeight,
                                                onComplete = {
                                                    currentIndex =
                                                        (currentIndex + 1) % words.size
                                                    isCenterFlipped = false
                                                    reviewedCount++
                                                }
                                            )
                                        }
                                    }
                                )

                                Column(
                                    modifier = Modifier,
                                    verticalArrangement = Arrangement.spacedBy(MioDimens.gapXs)
                                ) {
                                    RatingChip(
                                        label = "模糊",
                                        iconRes = R.drawable.ic_level_blurry,
                                        onClick = {
                                            scope.launch {
                                                showRatingButtons = false
                                                launch {
                                                    surfaceAlpha.animateTo(
                                                        0f,
                                                        tween(AnimationUtils.DURATION_SHORT)
                                                    )
                                                }
                                                launch { surfaceWidth.animateTo(48f, spring()) }
                                                launch { ratingButtonY.animateTo(300f, spring()) }
                                                exitCard(
                                                    offsetY, tiltZ, scale, screenHeight,
                                                    onComplete = {
                                                        currentIndex =
                                                            (currentIndex + 1) % words.size
                                                        isCenterFlipped = false
                                                        reviewedCount++
                                                    }
                                                )
                                            }
                                        }
                                    )

                                    RatingChip(
                                        label = "简单",
                                        iconRes = R.drawable.ic_level_easy,
                                        onClick = {
                                            scope.launch {
                                                showRatingButtons = false
                                                launch {
                                                    surfaceAlpha.animateTo(
                                                        0f,
                                                        tween(AnimationUtils.DURATION_SHORT)
                                                    )
                                                }
                                                launch { surfaceWidth.animateTo(48f, spring()) }
                                                launch { ratingButtonY.animateTo(300f, spring()) }
                                                exitCard(
                                                    offsetY, tiltZ, scale, screenHeight,
                                                    onComplete = {
                                                        currentIndex =
                                                            (currentIndex + 1) % words.size
                                                        isCenterFlipped = false
                                                        reviewedCount++
                                                    }
                                                )
                                            }
                                        }
                                    )

                                }

                                RatingChip(
                                    label = "认识",
                                    iconRes = R.drawable.ic_level_know,
                                    onClick = {
                                        scope.launch {
                                            showRatingButtons = false
                                            launch {
                                                surfaceAlpha.animateTo(
                                                    0f,
                                                    tween(AnimationUtils.DURATION_SHORT)
                                                )
                                            }
                                            launch { surfaceWidth.animateTo(48f, spring()) }
                                            launch { ratingButtonY.animateTo(300f, spring()) }
                                            exitCard(
                                                offsetY, tiltZ, scale, screenHeight,
                                                onComplete = {
                                                    currentIndex =
                                                        (currentIndex + 1) % words.size
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

@Composable
private fun EmptyStudyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(MioDimens.gapXxl),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "这个词书还没有单词",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
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
        launch { offsetY.animateTo(targetY, tween(AnimationUtils.DURATION_MEDIUM)) }
        launch { tiltZ.animateTo(randomAngle, tween(AnimationUtils.DURATION_MEDIUM)) }
        launch { scale.animateTo(0.8f, tween(AnimationUtils.DURATION_MEDIUM)) }
    }
    onComplete()
}

@Composable
private fun RatingChip(
    label: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .widthIn(min = 88.dp)
            .height(52.dp),
        onClick = onClick,
        shape = RoundedCornerShape(MioDimens.radiusPill),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}
