package com.akira.miokotoba.model

/**
 * 集中管理测试数据（临时方案，后续由 Repository 替代）
 */
object SampleData {
    private val n5Words = listOf(
        Word("n5-1", kanji = "朝", kana = "あさ", romaji = "asa", meaning = "早晨", mastered = true),
        Word("n5-2", kanji = "学校", kana = "がっこう", romaji = "gakkou", meaning = "学校", mastered = true),
        Word("n5-3", kanji = "図書館", kana = "としょかん", romaji = "toshokan", meaning = "图书馆", mastered = false),
        Word("n5-4", kanji = "食べる", kana = "たべる", romaji = "taberu", meaning = "吃", mastered = true),
        Word("n5-5", kanji = "飲む", kana = "のむ", romaji = "nomu", meaning = "喝", mastered = false),
        Word("n5-6", kanji = null, kana = "ありがとう", romaji = "arigatou", meaning = "谢谢", mastered = true),
        Word("n5-7", kanji = null, kana = "すみません", romaji = "sumimasen", meaning = "不好意思；对不起", mastered = false),
        Word("n5-8", kanji = "国際関係論", kana = "こくさいかんけいろん", romaji = "kokusai kankei ron", meaning = "国际关系论", mastered = false),
        Word("n5-9", kanji = "日曜日", kana = "にちようび", romaji = "nichiyoubi", meaning = "星期日", mastered = false),
        Word("n5-10", kanji = "小さい", kana = "ちいさい", romaji = "chiisai", meaning = "小的", mastered = false),
    )

    private val conversationWords = listOf(
        Word("conv-1", kanji = "お願いします", kana = "おねがいします", romaji = "onegaishimasu", meaning = "拜托了；麻烦您", mastered = true),
        Word("conv-2", kanji = "大丈夫", kana = "だいじょうぶ", romaji = "daijoubu", meaning = "没关系；不要紧", mastered = true),
        Word("conv-3", kanji = null, kana = "ちょっと", romaji = "chotto", meaning = "稍微；有点", mastered = false),
        Word("conv-4", kanji = "久しぶり", kana = "ひさしぶり", romaji = "hisashiburi", meaning = "好久不见", mastered = false),
        Word("conv-5", kanji = "失礼します", kana = "しつれいします", romaji = "shitsurei shimasu", meaning = "告辞；打扰了", mastered = false),
        Word("conv-6", kanji = null, kana = "なるほど", romaji = "naruhodo", meaning = "原来如此", mastered = true),
        Word("conv-7", kanji = "申し訳ありません", kana = "もうしわけありません", romaji = "moushiwake arimasen", meaning = "非常抱歉", mastered = false),
    )

    private val travelWords = listOf(
        Word("travel-1", kanji = "切符", kana = "きっぷ", romaji = "kippu", meaning = "车票", mastered = true),
        Word("travel-2", kanji = "乗り換え", kana = "のりかえ", romaji = "norikae", meaning = "换乘", mastered = false),
        Word("travel-3", kanji = "改札口", kana = "かいさつぐち", romaji = "kaisatsuguchi", meaning = "检票口", mastered = false),
        Word("travel-4", kanji = "片道", kana = "かたみち", romaji = "katamichi", meaning = "单程", mastered = false),
        Word("travel-5", kanji = "往復", kana = "おうふく", romaji = "oufuku", meaning = "往返", mastered = true),
        Word("travel-6", kanji = "予約番号", kana = "よやくばんごう", romaji = "yoyaku bangou", meaning = "预约号码", mastered = false),
        Word("travel-7", kanji = null, kana = "チェックイン", romaji = "chekku in", meaning = "办理入住；值机", mastered = false),
        Word("travel-8", kanji = "非常口", kana = "ひじょうぐち", romaji = "hijouguchi", meaning = "紧急出口", mastered = false),
        Word("travel-9", kanji = "手荷物受取所", kana = "てにもつうけとりじょ", romaji = "tenimotsu uketorijo", meaning = "行李领取处", mastered = false),
    )

    private val katakanaWords = listOf(
        Word("loan-1", kanji = null, kana = "コンビニ", romaji = "konbini", meaning = "便利店", mastered = true),
        Word("loan-2", kanji = null, kana = "エレベーター", romaji = "erebeetaa", meaning = "电梯", mastered = false),
        Word("loan-3", kanji = null, kana = "ミーティング", romaji = "miitingu", meaning = "会议", mastered = false),
        Word("loan-4", kanji = null, kana = "スケジュール", romaji = "sukejuuru", meaning = "日程；计划表", mastered = false),
        Word("loan-5", kanji = null, kana = "アプリケーション", romaji = "apurikeeshon", meaning = "应用程序", mastered = false),
        Word("loan-6", kanji = null, kana = "コミュニケーション", romaji = "komyunikeeshon", meaning = "沟通；交流", mastered = false),
    )

    val books = listOf(
        WordBook(
            id = "1",
            title = "N5 核心词汇",
            description = "短词、基础动词和常用表达混合测试",
            wordCount = n5Words.size,
            learnedCount = n5Words.count { it.mastered }
        ),
        WordBook(
            id = "2",
            title = "日常会话表达",
            description = "口语表达、寒暄和礼貌说法",
            wordCount = conversationWords.size,
            learnedCount = conversationWords.count { it.mastered }
        ),
        WordBook(
            id = "3",
            title = "旅行交通",
            description = "车站、机场、酒店场景常用词",
            wordCount = travelWords.size,
            learnedCount = travelWords.count { it.mastered }
        ),
        WordBook(
            id = "4",
            title = "外来语长词",
            description = "片假名词、长罗马音和长释义排版测试",
            wordCount = katakanaWords.size,
            learnedCount = katakanaWords.count { it.mastered }
        ),
    )

    /** 按词本 ID 组织的单词数据 */
    val words: Map<String, List<Word>> = mapOf(
        "1" to n5Words,
        "2" to conversationWords,
        "3" to travelWords,
        "4" to katakanaWords,
    )

    /** 根据 bookId 获取单词列表 */
    fun wordsForBook(bookId: String): List<Word> = words[bookId] ?: emptyList()

    /** 根据 bookId 获取词本 */
    fun bookById(bookId: String): WordBook? = books.find { it.id == bookId }
}
