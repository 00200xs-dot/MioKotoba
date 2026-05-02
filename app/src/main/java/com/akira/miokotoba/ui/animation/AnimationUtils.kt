package com.akira.miokotoba.ui.animation

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing

object AnimationUtils {
    /**
     *短动画时长 150ms
     *适用于：图标旋转、按钮状态变化、小型元素
     */
    const val DURATION_SHORT = 150

    /**中动画时长 300ms
     *适用于：列表项、卡片展开、对话框显示
     */
    const val DURATION_MEDIUM = 300

    /**长动画时长 500ms
     *适用于：页面转场、大型元素移动
     */
    const val DURATION_LONG = 500

    /**动画延迟基数 50ms
     *用于计算列表项交错动画的延迟
     */
    const val DURATION_DELAY_BASE = 50

    /**
     * 标准过渡缓动
     */
    val standardEasing = FastOutSlowInEasing

    /**
     *进入动画缓动
     */
    val enterEasing = LinearOutSlowInEasing

    /**
     *退出动画缓动
     */
    val exitEasing = FastOutLinearInEasing


}