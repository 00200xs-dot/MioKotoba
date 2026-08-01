package com.akira.miokotoba.domain.review

/**
 * 一次 SM-2 计算后的复习参数
 *
 * @param repetitions 连续正确复习次数
 * @param easeFactor 易度因子, 最低为 1.3
 * @param intervalDays 距离下次复习的天数
 */
data class Sm2Result(
    val repetitions: Int,
    val easeFactor: Double,
    val intervalDays: Int
)