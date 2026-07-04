package com.akira.miokotoba.ui.design

import androidx.compose.ui.unit.dp

/**
 * MioKotoba 设计系统的基础尺寸 token。
 *
 * 这个文件定义稳定、跨页面复用的 UI 尺寸。
 * 使用时优先从这里取值，避免在页面里临时写 13.dp、17.dp 这类难以维护的数字。
 *
 * 分层原则：
 * - [MioSpacing]：元素之间的距离，例如 padding、gap、列表间距。
 * - [MioRadius]：形状圆角，例如卡片、列表项、胶囊按钮。
 * - [MioSize]：固定尺寸，例如图标、TopBar、主按钮高度。
 */

/**
 * 空间距离 token。
 *
 * 使用规范：
 * - 页面左右边距使用 [pageHorizontal]。
 * - 页面顶部/底部内容留白使用 [pageVertical]。
 * - 组件内部 padding 通常使用 [md]、[lg] 或 [xl]。
 * - 区块之间的垂直间距通常使用 [xl] 或 [xxl]。
 */
object MioSpacing {
    /** 页面内容的标准水平边距。 */
    val pageHorizontal = 20.dp

    /** 页面内容的标准垂直边距。 */
    val pageVertical = 16.dp

    /** 4dp：极小间距，用于图标和短文本之间、紧凑元素内部。 */
    val xs = 4.dp

    /** 8dp：小间距，用于紧凑 Row/Column 或辅助信息之间。 */
    val sm = 8.dp

    /** 12dp：中小间距，用于列表项内部、较近的信息组。 */
    val md = 12.dp

    /** 16dp：标准间距，用于普通卡片内边距和常规组件间距。 */
    val lg = 16.dp

    /** 20dp：较大间距，用于主要卡片内边距和页面主内容。 */
    val xl = 20.dp

    /** 24dp：区块级间距，用于 Section 之间或大容器内部。 */
    val xxl = 24.dp

    /** 32dp：大面积空状态或需要更强呼吸感的页面留白。 */
    val xxxl = 32.dp
}

/**
 * 形状圆角 token。
 *
 * 使用规范：
 * - 普通列表项优先使用 [lg]。
 * - 大卡片和页面容器优先使用 [xl]。
 * - 搜索栏、顶部操作区、主按钮等胶囊形元素使用 [pill]。
 * - 学习卡片是产品特色组件，保留独立的 [studyCard]。
 *
 * 注意：不要把所有东西都做成 pill。
 */
object MioRadius {
    /** 12dp：小型容器、紧凑控件。 */
    val sm = 12.dp

    /** 16dp：标准控件、普通卡片。 */
    val md = 16.dp

    /** 20dp：列表项、工具入口、设置项。 */
    val lg = 20.dp

    /** 24dp：页面主要卡片、大容器、浮层面板。 */
    val xl = 24.dp

    /** 28dp：胶囊形操作区、搜索栏、主按钮。 */
    val pill = 28.dp

    /** 45dp：学习卡片专用圆角，保留更强的产品个性。 */
    val studyCard = 45.dp
}

/**
 * 固定尺寸 token。
 *
 * 使用规范：
 * - 图标本体尺寸使用 [iconSm]、[iconMd]、[iconLg]。
 * - 图标背景槽使用 [iconContainer]，例如列表项左侧的识别图标。
 * - 顶栏、胶囊按钮、列表项和主按钮高度统一从这里取。
 *
 * 固定尺寸和 spacing 的区别：
 * - spacing 是“两个东西之间隔多远”。
 * - size 是“这个东西本身应该多大”。
 */
object MioSize {
    /** 18dp：辅助图标，例如列表副信息、状态标识。 */
    val iconSm = 18.dp

    /** 22dp：标准图标，例如 TopBar 操作、普通按钮图标。 */
    val iconMd = 22.dp

    /** 28dp：强调图标，例如空状态或大入口图标。 */
    val iconLg = 28.dp

    /** 44dp：列表项或卡片中的图标容器尺寸。 */
    val iconContainer = 44.dp

    /** 88dp：Mio 顶栏最小高度，包含状态栏后的视觉高度基准。 */
    val topBarMinHeight = 88.dp

    /** 64dp：二级页面紧凑顶栏高度，用于返回页、详情页等。 */
    val topBarCompactMinHeight = 64.dp

    /** 56dp：右上角胶囊操作区和搜索栏的标准高度。 */
    val actionPillHeight = 56.dp

    /** 72dp：普通列表项最小高度，保证触控和信息密度平衡。 */
    val listItemMinHeight = 72.dp

    /** 56dp：页面主操作按钮高度。 */
    val primaryButtonHeight = 56.dp

    /** 60dp：悬浮操作按钮底部留白，用于避开底部导航区域。 */
    val fabBottomSpace = 60.dp

    /** 8dp：学习卡片等强调容器的默认阴影高度。 */
    val cardShadow = 8.dp

    /** 2dp：学习卡片正反面装饰线高度。 */
    val decorLineHeight = 2.dp
}
