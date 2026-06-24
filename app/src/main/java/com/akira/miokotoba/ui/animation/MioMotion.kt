package com.akira.miokotoba.ui.animation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

object MioMotion {
    const val Quick = 150
    const val Standard = 240
    const val Emphasized = 320

    val StandardEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    val ExitEasing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)

    fun <T> quickTween() = tween<T>(durationMillis = Quick, easing = StandardEasing)
    fun <T> standardTween() = tween<T>(durationMillis = Standard, easing = StandardEasing)
    fun <T> emphasizedTween() = tween<T>(durationMillis = Emphasized, easing = StandardEasing)
    fun <T> exitTween() = tween<T>(durationMillis = Quick, easing = ExitEasing)

    fun gentleSpring() = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    fun expressiveSpring() = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
}
