package com.akira.miokotoba.model

/**
 * 集中管理测试数据（临时方案，后续由 Repository 替代）
 */
object SampleData {
    val books = listOf(
        WordBook(
            id = "1",
            title = "N5 核心词汇",
            description = "日语能力考 N5 必备单词",
            wordCount = 6,
            learnedCount = 2
        ),
        WordBook(
            id = "2",
            title = "日常会话表达",
            description = "日常生活常用口语",
            wordCount = 2,
            learnedCount = 1
        ),
    )

    /** 按词本 ID 组织的单词数据 */
    val words: Map<String, List<Word>> = mapOf(
        "1" to listOf(
            Word("w1", kanji = "亜", kana = "あ", romaji = "a", meaning = "亚洲", mastered = true),
            Word("w2", kanji = "伊", kana = "い", romaji = "i", meaning = "意大利", mastered = true),
            Word("w3", kanji = "宇", kana = "う", romaji = "u", meaning = "宇宙", mastered = false),
            Word("w4", kanji = "江", kana = "え", romaji = "e", meaning = "海湾", mastered = false),
            Word("w5", kanji = "於", kana = "お", romaji = "o", meaning = "于", mastered = false),
            Word("w6", kanji = "加", kana = "か", romaji = "ka", meaning = "增加", mastered = false),
        ),
        "2" to listOf(
            Word("w7", kanji = "食べる", kana = "たべる", romaji = "taberu", meaning = "吃", mastered = true),
            Word("w8", kanji = "飲む", kana = "のむ", romaji = "nomu", meaning = "喝", mastered = false),
        ),
    )

    /** 根据 bookId 获取单词列表 */
    fun wordsForBook(bookId: String): List<Word> = words[bookId] ?: emptyList()

    /** 根据 bookId 获取词本 */
    fun bookById(bookId: String): WordBook? = books.find { it.id == bookId }
}
