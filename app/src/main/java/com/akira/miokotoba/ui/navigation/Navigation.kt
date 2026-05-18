package com.akira.miokotoba.ui.navigation

import com.akira.miokotoba.R

enum class Navigation(
    val route: String,   //不启用 预留
    val label: String,
    val iconRes: Int
) {
    Library("Library", "单词本", R.drawable.ic_nav_library_book),
    Study("Study", "记忆", R.drawable.ic_nav_study_playarrow),
    Settings("Settings", "设置", R.drawable.ic_nav_settings_settings)
}