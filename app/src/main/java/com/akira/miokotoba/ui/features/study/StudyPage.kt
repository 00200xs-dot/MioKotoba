package com.akira.miokotoba.ui.features.study

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.R
import com.akira.miokotoba.model.ReviewState
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.ui.design.MioMotion
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.design.MioSpacing
import com.akira.miokotoba.ui.features.study.components.WordCard
import com.akira.miokotoba.ui.modifier.tiltOnTouch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun StudyPage(
    words: List<Word>,
    onWordReviewed: (Word, ReviewState) -> Unit,
    onBack: (() -> Unit)? = null
) {
    if (words.isEmpty()) {
        EmptyStudyContent()
        return
    }

    var isFlipped by rememberSaveable { mutableStateOf(false) }
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var reviewedCount by rememberSaveable { mutableIntStateOf(0) }
    // 词卡是否正在移动
    var isCardMoving by remember { mutableStateOf(false) }
    // 当前单词
    val currentWord = words[currentIndex.coerceIn(words.indices)]

    val offsetY = remember { androidx.compose.animation.core.Animatable(0f) }
    val scale = remember { androidx.compose.animation.core.Animatable(1f) }
    val tiltZ = remember { androidx.compose.animation.core.Animatable(0f) }
    val scope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MioSpacing.lg)
    ) {
        val screenHeight = constraints.maxHeight.toFloat()

        LaunchedEffect(currentIndex) {
            isCardMoving = true
            scale.snapTo(1f)
            tiltZ.snapTo(0f)
            offsetY.snapTo(-screenHeight)
            offsetY.animateTo(0f, MioMotion.studyCardEnterTween())
            isCardMoving = false
        }

        Box(modifier = Modifier.fillMaxSize()) {
            StudySessionHeader(
                reviewedCount = reviewedCount.coerceAtMost(words.size),
                totalCount = words.size,
                onBack = onBack,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 72.dp, bottom = 112.dp),
                contentAlignment = Alignment.Center
            ) {
                WordCard(
                    modifier = Modifier
                        .offset { IntOffset(0, offsetY.value.roundToInt()) }
                        .scale(scale.value)
                        .tiltOnTouch()
                        .graphicsLayer { rotationZ = tiltZ.value },
                    word = currentWord,
                    isFlipped = isFlipped,
                    onCardClick = {
                        if (!isFlipped && !isCardMoving) {
                            isFlipped = true
                        }
                    }
                )
            }

            RatingBar(
                visible = isFlipped,
                modifier = Modifier.align(Alignment.BottomCenter),
                onRate = { selectedState ->
                    // 判断卡片是否在移动, 避免用户过快点击
                    if (isCardMoving) return@RatingBar
                    isCardMoving = true

                    // 通知上层当前的单词以及用户选择的状态
                    onWordReviewed(currentWord, selectedState)

                    // 卡片退出动画
                    scope.launch {
                        isFlipped = false
                        exitCard(
                            offsetY = offsetY,
                            tiltZ = tiltZ,
                            scale = scale,
                            targetY = screenHeight,
                            onComplete = {
                                currentIndex = (currentIndex + 1) % words.size
                                reviewedCount++
                            }
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun StudySessionHeader(
    reviewedCount: Int,
    totalCount: Int,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val progress = if (totalCount > 0) reviewedCount.toFloat() / totalCount.toFloat() else 0f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = MioSpacing.md),
        verticalArrangement = Arrangement.spacedBy(MioSpacing.sm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "返回",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Text(
                text = "学习中",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$reviewedCount / $totalCount",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
private fun RatingBar(
    visible: Boolean,
    modifier: Modifier = Modifier,
    onRate: (ReviewState) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically(MioMotion.emphasizedTween()) { it / 3 } + fadeIn(MioMotion.standardTween()),
        exit = slideOutVertically(MioMotion.exitTween()) { it / 4 } + fadeOut(MioMotion.exitTween())
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = MioSpacing.lg),
            shape = RoundedCornerShape(MioRadius.lg),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            tonalElevation = 4.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(MioSpacing.sm),
                horizontalArrangement = Arrangement.spacedBy(MioSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReviewLevel.entries.forEach { level ->
                    RatingChip(
                        level = level,
                        onClick = { onRate(level.reviewState) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RatingChip(
    level: ReviewLevel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(62.dp),
        onClick = onClick,
        shape = RoundedCornerShape(MioRadius.md),
        color = level.containerColor(),
        contentColor = level.contentColor(),
        border = BorderStroke(1.dp, level.contentColor().copy(alpha = 0.16f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = MioSpacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = level.iconRes),
                contentDescription = level.label,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = level.label,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun EmptyStudyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(MioSpacing.xxl),
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

private suspend fun exitCard(
    offsetY: androidx.compose.animation.core.Animatable<Float, *>,
    tiltZ: androidx.compose.animation.core.Animatable<Float, *>,
    scale: androidx.compose.animation.core.Animatable<Float, *>,
    targetY: Float,
    onComplete: () -> Unit
) {
    val randomAngle = Random.nextFloat() * 6f - 3f
    coroutineScope {
        launch { offsetY.animateTo(targetY, MioMotion.studyCardExitTween()) }
        launch { tiltZ.animateTo(randomAngle, MioMotion.studyCardExitTween()) }
        launch { scale.animateTo(0.8f, MioMotion.studyCardExitTween()) }
    }
    onComplete()
}

private enum class ReviewLevel(
    val label: String,
    val iconRes: Int,
    val color: Color,
    val reviewState: ReviewState
) {
    Again("不认识", R.drawable.ic_level_not_know, Color(0xFFB3261E), ReviewState.Again),
    Vague("模糊", R.drawable.ic_level_blurry, Color(0xFF8A5A00), ReviewState.Vague),
    Know("认识", R.drawable.ic_level_know, Color(0xFF146C43), ReviewState.Know),
    Easy("简单", R.drawable.ic_level_easy, Color(0xFF005CBB), ReviewState.Easy);
}

@Composable
private fun ReviewLevel.containerColor(): Color {
    return color.copy(alpha = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) 0.12f else 0.22f)
}

@Composable
private fun ReviewLevel.contentColor(): Color = color

private fun Color.luminance(): Float {
    fun linear(channel: Float): Float {
        return if (channel <= 0.03928f) {
            channel / 12.92f
        } else {
            ((channel + 0.055f) / 1.055f).toDouble().pow(2.4).toFloat()
        }
    }
    return 0.2126f * linear(red) + 0.7152f * linear(green) + 0.0722f * linear(blue)
}
