package com.akira.miokotoba.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akira.miokotoba.model.ReviewState

/**
 * 单词数据库实体
 *
 * - [id]单词唯一标识
 * - [bookId]用于标识单词所在的词书
 * - [kanji]汉字, 日语单词可能不含有汉字所以可为空
 * - [kana]假名
 * - [romaji]罗马字读音
 * - [meaning]单词释义
 * - [reviewState]复习状态, 有四种状态:**Again不认识 Vague模糊 Know认识 Easy简单**
 * - [repetitions]单词上次没有成功回忆以来连续成功回忆的次数 n
 * - [easeFactor]易度因子 EF, 数字越大代表单词越容易, 影响重复间隔 I 的增长速度
 * - [intervalDays]重复间隔 I, 距离下次提醒复习的时间, 单位为 **天**
 * - [nextReviewAt]下次复习时间, 存储时间戳
 * - [lastReviewedAt] 上次复习时间, 存储时间戳, 可为空
 */
@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey val id: String,
    val bookId: String,
    val kanji: String?,
    val kana: String,
    val romaji: String,
    val meaning: String,

    val reviewState: ReviewState,

    val repetitions: Int,
    val easeFactor: Double,
    val intervalDays: Int,
    val nextReviewAt: Long,
    val lastReviewedAt: Long?
)
