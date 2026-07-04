package com.akira.miokotoba.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.ui.design.MioMotion
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.design.MioSize
import com.akira.miokotoba.ui.design.MioSpacing
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun MioSwipeActionItem(
    rightAction: MioSwipeAction,
    leftAction: MioSwipeAction,
    modifier: Modifier = Modifier,
    actionWidth: Dp = 80.dp,
    overTriggerDistance: Dp = 28.dp,
    contentHorizontalPadding: Dp = MioSpacing.lg,
    content: @Composable (Modifier) -> Unit
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var itemWidthPx by remember { mutableStateOf(0f) }
    var hasTriggeredAction by remember { mutableStateOf(false) }
    val actionWidthPx = with(LocalDensity.current) { actionWidth.toPx() }
    val overTriggerPx = with(LocalDensity.current) { overTriggerDistance.toPx() }
    val currentOffset = offsetX.value
    val thresholdPx = if (itemWidthPx > 0f) {
        maxOf(itemWidthPx * 0.2f, actionWidthPx)
    } else {
        actionWidthPx
    }
    val progress = (abs(currentOffset) / thresholdPx).coerceIn(0f, 1f)
    val backgroundAlpha = progress * 0.75f
    val contentAlpha = (progress * 1.4f).coerceIn(0f, 1f)

    fun resetCard() {
        scope.launch {
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = MioMotion.gentleSpring()
            )
        }
    }

    fun triggerAction(action: MioSwipeAction, targetOffset: Float) {
        if (hasTriggeredAction) return
        hasTriggeredAction = true

        scope.launch {
            offsetX.animateTo(
                targetValue = targetOffset,
                animationSpec = MioMotion.gentleSpring()
            )

            action.onTriggered()

            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = MioMotion.gentleSpring()
            )
            hasTriggeredAction = false
        }
    }

    fun applyResistance(current: Float, delta: Float): Float {
        val distanceProgress = (abs(current) / thresholdPx).coerceIn(0f, 1f)
        val resistance = when {
            distanceProgress < 0.55f -> 1f
            distanceProgress < 0.85f -> 0.72f
            else -> 0.42f
        }
        val next = current + delta * resistance

        return when {
            next > thresholdPx -> thresholdPx + (next - thresholdPx) * 0.24f
            next < -thresholdPx -> -thresholdPx + (next + thresholdPx) * 0.24f
            else -> next
        }
    }

    fun settleCard() {
        when {
            offsetX.value >= thresholdPx -> triggerAction(rightAction, thresholdPx)
            offsetX.value <= -thresholdPx -> triggerAction(leftAction, -thresholdPx)
            else -> resetCard()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { itemWidthPx = it.width.toFloat() }
    ) {
        if (currentOffset > 0f) {
            MioSwipeActionSurface(
                action = rightAction,
                alignment = Alignment.CenterStart,
                backgroundAlpha = backgroundAlpha,
                contentAlpha = contentAlpha,
                actionWidth = actionWidth,
                contentHorizontalPadding = contentHorizontalPadding,
                modifier = Modifier.matchParentSize()
            )
        } else if (currentOffset < 0f) {
            MioSwipeActionSurface(
                action = leftAction,
                alignment = Alignment.CenterEnd,
                backgroundAlpha = backgroundAlpha,
                contentAlpha = contentAlpha,
                actionWidth = actionWidth,
                contentHorizontalPadding = contentHorizontalPadding,
                modifier = Modifier.matchParentSize()
            )
        }

        content(
            Modifier
                .offset { IntOffset(currentOffset.roundToInt(), 0) }
                .pointerInput(thresholdPx) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            val proposedOffset = offsetX.value + dragAmount

                            if (proposedOffset >= thresholdPx + overTriggerPx) {
                                triggerAction(rightAction, thresholdPx)
                            } else if (proposedOffset <= -thresholdPx - overTriggerPx) {
                                triggerAction(leftAction, -thresholdPx)
                            } else {
                                scope.launch {
                                    offsetX.snapTo(applyResistance(offsetX.value, dragAmount))
                                }
                            }
                        },
                        onDragEnd = { settleCard() },
                        onDragCancel = { settleCard() }
                    )
                }
        )
    }
}

data class MioSwipeAction(
    val iconRes: Int,
    val label: String,
    val containerColor: Color,
    val contentColor: Color,
    val contentDescription: String,
    val onTriggered: () -> Unit
)

@Composable
private fun MioSwipeActionSurface(
    action: MioSwipeAction,
    alignment: Alignment,
    backgroundAlpha: Float,
    contentAlpha: Float,
    actionWidth: Dp,
    contentHorizontalPadding: Dp,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(horizontal = contentHorizontalPadding),
        color = action.containerColor.copy(alpha = backgroundAlpha),
        shape = RoundedCornerShape(MioRadius.lg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = contentHorizontalPadding),
            contentAlignment = alignment
        ) {
            Column(
                modifier = Modifier.width(actionWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MioSpacing.xs)
            ) {
                Icon(
                    painter = painterResource(action.iconRes),
                    contentDescription = action.contentDescription,
                    tint = action.contentColor.copy(alpha = contentAlpha),
                    modifier = Modifier.size(MioSize.iconMd)
                )
                Text(
                    text = action.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = action.contentColor.copy(alpha = contentAlpha)
                )
            }
        }
    }
}
