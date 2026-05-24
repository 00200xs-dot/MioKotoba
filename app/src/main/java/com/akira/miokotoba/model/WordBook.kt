package com.akira.miokotoba.model

/**
 * 单词本
 */
data class WordBook(
    val id: String, //唯一标识
    var title: String,  //标题
    var description: String,    //词本描述
    val wordCount: Int, //单词总数
    var learnedCount: Int,  //已经学习单词数
)

/**
 * 单词
 */
data class Word(
    val id: String, //唯一标识
    var kanji: String,  //日文汉字
    var kana: String,   //假名读音
    var meaning: String,    //中文释义
    var mastered: Boolean   //是否掌握
)