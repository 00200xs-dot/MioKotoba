package com.akira.miokotoba.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 词本数据库实体
 *
 * - [id] 词本唯一标识
 * - [title] 词本标题
 * - [description] 词本描述, 用于说明词本内容或学习场景
 * - [wordCount] 词本内单词总数, 作为列表页展示用的缓存统计值
 * - [learnedCount] 已学习单词数, 具体统计规则由复习算法决定
 */
@Entity(tableName = "word_books")
data class WordBookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val wordCount: Int,
    val learnedCount: Int
)
