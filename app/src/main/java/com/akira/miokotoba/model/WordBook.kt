package com.akira.miokotoba.model

/**
 * 单词本
 */
data class WordBook(
    val id: String, //唯一标识
    val title: String,  //标题
    val description: String,    //词本描述
    val wordCount: Int, //单词总数
    val learnedCount: Int,  //已经学习单词数
) : java.io.Serializable

/**
 * 单词
 */
data class Word(
    val id: String,         // 唯一标识
    val kanji: String?,     // 汉字写法，纯假名词为 null
    val kana: String,       // 假名读音
    val romaji: String,     // 罗马音
    val meaning: String,    // 中文释义
    val mastered: Boolean   // 是否掌握
) : java.io.Serializable
