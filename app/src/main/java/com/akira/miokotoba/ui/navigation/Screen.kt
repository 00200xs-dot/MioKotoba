package com.akira.miokotoba.ui.navigation

sealed class Screen(val route: String) {
    /** 主页（Tab 容器：单词本、学习入口、设置） */
    object Main : Screen("main")
    object WordBookDetail : Screen("wordbook_detail/{bookId}") {
        fun createRoute(bookId: String) = "wordbook_detail/$bookId"
    }
    object StudySession : Screen("study_session/{bookId}") {
        fun createRoute(bookId: String) = "study_session/$bookId"
    }
    object KanaChart : Screen("kana_chart")
}