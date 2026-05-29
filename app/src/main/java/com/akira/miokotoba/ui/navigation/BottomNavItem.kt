package com.akira.miokotoba.ui.navigation

import com.akira.miokotoba.R

/** 底部导航栏项目，不再持有 route（路由统一由 Screen 管理） */
enum class BottomNavItem(
    val label: String,
    val iconRes: Int
) {
    WordBook("单词本", R.drawable.ic_nav_library_book),
    Study("记忆", R.drawable.ic_nav_study_playarrow),
    Settings("设置", R.drawable.ic_nav_settings_settings)
}