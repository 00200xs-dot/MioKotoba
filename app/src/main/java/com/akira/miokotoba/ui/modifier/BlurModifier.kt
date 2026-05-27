package com.akira.miokotoba.ui.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 按条件模糊内容 — 用于弹出遮罩时模糊背景。
 */
fun Modifier.blurIf(
    condition: Boolean,
    radius: Dp = 10.dp
): Modifier = if (condition) this.blur(radius) else this