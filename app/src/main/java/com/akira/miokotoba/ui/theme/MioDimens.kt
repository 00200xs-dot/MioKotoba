package com.akira.miokotoba.ui.theme

import androidx.compose.ui.unit.dp

/**
 * MioKotoba 统一设计尺寸规范
 * 遵循 4dp 网格体系，参考 Material Design 3 间距
 *
 * 圆角: xs(4) → sm(8) → md(12) → lg(16) → xl(20) → xxl(24) → pill(28) → card(45)
 * 间距: xs(4) → sm(8) → md(12) → lg(16) → xl(20) → xxl(24) → xxxl(32)
 */
object MioDimens {
    // ==================== 圆角 ====================

    /** 微型圆角 4dp — 指示器、紧凑标签 */
    val radiusXs = 4.dp
    /** 小圆角 8dp — 缩略图、骨架屏占位 */
    val radiusSm = 8.dp
    /** 中圆角 12dp — InfoChip、RatingChip、筛选标签 */
    val radiusMd = 12.dp
    /** 大圆角 16dp — 标准卡片、输入框、评级栏 Surface */
    val radiusLg = 16.dp
    /** 加大圆角 20dp — 大容器、选择器卡片 */
    val radiusXl = 20.dp
    /** 超大圆角 24dp — BookCard 词本卡片 */
    val radiusXxl = 24.dp
    /** 胶囊圆角 28dp — TopBar 搜索胶囊 */
    val radiusPill = 28.dp
    /** 卡片圆角 45dp — WordCard 学习卡片 */
    val radiusCard = 45.dp

    // ==================== 间距 ====================

    /** 微间距 4dp — 图标组间距、紧凑元素内边距 */
    val gapXs = 4.dp
    /** 小间距 8dp — Chip 垂直内边距、紧凑 Row 排列 */
    val gapSm = 8.dp
    /** 中间距 12dp — LazyColumn 列表项间距、卡片内容区段 */
    val gapMd = 12.dp
    /** 大间距 16dp — 页面外边距、卡片内边距、Section 间距 */
    val gapLg = 16.dp
    /** 加大间距 20dp — BookCard / Sheet 大容器内边距 */
    val gapXl = 20.dp
    /** 超大间距 24dp — TopBar 水平内边距、Sheet 内容区、WordCard 内容区 */
    val gapXxl = 24.dp
    /** 特大间距 32dp — 空状态页、认证页面 */
    val gapXxxl = 32.dp

    // ==================== 组件专用 ====================

    /** TopBar 水平内边距 */
    val topBarHorizontal = 24.dp
    /** TopBar 垂直内边距 */
    val topBarVertical = 16.dp
    /** FAB 底部留白 60dp — 避开底部导航栏 */
    val fabBottomSpace = 60.dp
    /** TopBar 最小高度 88dp */
    val topBarMinHeight = 88.dp
    /** 搜索胶囊高度 56dp */
    val searchPillHeight = 56.dp
    /** 卡片默认阴影高度 8dp */
    val cardShadow = 8.dp
    /** 装饰线高度 2dp — WordCard 正反面上下装饰线 */
    val decorLineHeight = 2.dp
}
