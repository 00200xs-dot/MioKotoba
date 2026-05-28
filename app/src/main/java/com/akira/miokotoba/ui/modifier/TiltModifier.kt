package com.akira.miokotoba.ui.modifier

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.spring
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.tiltOnTouch(): Modifier = composed {
    val angleX = remember { Animatable(0f) }
    val angleY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    pointerInput(Unit) {
        detectDragGestures(
            onDrag = { change, _ ->
                change.consume()
                scope.launch {
                    angleX.snapTo((change.position.y / size.height - 0.5f) * 10f)
                    angleY.snapTo((change.position.x / size.width - 0.5f) * 10f)
                }
            },
            onDragEnd = {
                scope.launch {
                    angleX.animateTo(0f, spring())
                    angleY.animateTo(0f, spring())
                }
            }
        )
    }.graphicsLayer {
        rotationX = angleX.value
        rotationY = angleY.value
    }
}