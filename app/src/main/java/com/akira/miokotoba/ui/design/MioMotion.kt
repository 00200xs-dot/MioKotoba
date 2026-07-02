package com.akira.miokotoba.ui.design

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

/**
 * MioKotoba 的动效 token。
 *
 * 这个对象是用来统一“什么状态变化应该怎么动”。
 */
object MioMotion {
    /**
     * 150ms：快速反馈。
     *
     * 适用场景：
     * - 图标切换
     * - 清除按钮出现/消失
     * - 按钮按压反馈
     * - 小型状态变化
     */
    const val quick = 150

    /**
     * 240ms：标准状态变化。
     *
     * 适用场景：
     * - 搜索栏展开的内容淡入
     * - FilterChip 选中态变化
     * - 列表项轻微进入
     * - 普通卡片状态变化
     */
    const val standard = 240

    /**
     * 300ms：强调型容器变化。
     *
     * 适用场景：
     * - 右上角操作胶囊展开成面板
     * - 设置面板出现
     * - 学习卡片进入
     * - 需要用户感知来源和去向的变化
     */
    const val emphasized = 300

    /**
     * 320ms：学习卡片入场。
     *
     * 卡片是学习页的核心对象，入场需要比普通组件更有存在感，但不能拖慢节奏。
     */
    const val studyCardEnter = 320

    /**
     * 380ms：学习卡片离场。
     *
     * 离场包含下落、缩放、轻微旋转，比普通退出动画稍长，避免“突然消失”的断裂感。
     */
    const val studyCardExit = 380

    /**
     * 320ms：页面级转场。
     *
     * 适用场景：
     * - 主页面进入/退出
     * - 子页面导航
     *
     * 注意：页面转场不要太慢。学习工具应该轻快，超过 400ms 容易显得拖沓。
     */
    const val page = 320

    /**
     * 标准进入/变化缓动。
     *
     * 特点：前段启动自然，后段稳定收束。适合大多数非弹性 UI 状态变化。
     */
    val standardEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

    /**
     * 退出缓动。
     *
     * 特点：更快离开，避免关闭动作显得拖泥带水。
     */
    val exitEasing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)

    /** 快速 tween，用于小反馈和轻量状态切换。 */
    fun <T> quickTween() = tween<T>(
        durationMillis = quick,
        easing = standardEasing
    )

    /** 标准 tween，用于大多数组件状态变化。 */
    fun <T> standardTween() = tween<T>(
        durationMillis = standard,
        easing = standardEasing
    )

    /** 强调 tween，用于容器变形、面板出现、较明显的空间变化。 */
    fun <T> emphasizedTween() = tween<T>(
        durationMillis = emphasized,
        easing = standardEasing
    )

    /** 学习卡片入场 tween，用于主卡片从上方进入并稳定落位。 */
    fun <T> studyCardEnterTween() = tween<T>(
        durationMillis = studyCardEnter,
        easing = standardEasing
    )

    /** 学习卡片离场 tween，用于评级后向下离开。 */
    fun <T> studyCardExitTween() = tween<T>(
        durationMillis = studyCardExit,
        easing = standardEasing
    )

    /** 退出 tween，用于关闭、隐藏、离场。 */
    fun <T> exitTween() = tween<T>(
        durationMillis = quick,
        easing = exitEasing
    )

    /**
     * 克制弹簧。
     *
     * 适用场景：
     * - 卡片轻微复位
     * - 不希望有弹跳感的尺寸/位置过渡
     *
     * 普通 UI 优先使用这个，而不是裸 spring()。
     */
    fun gentleSpring() = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    /**
     * 表现型弹簧。
     *
     * 适用场景：
     * - 学习卡片入场
     * - 少数核心对象的轻微个性动画
     *
     * 注意：不要把它用于 TopBar、列表项、设置项等框架 UI，否则界面会显得吵。
     */
    fun expressiveSpring() = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
}
