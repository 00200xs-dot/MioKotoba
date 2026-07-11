package com.akira.miokotoba.data.local

import androidx.room.TypeConverter
import com.akira.miokotoba.model.ReviewState

/**
 * ReviewState 与数据库字段之间的转换器
 *
 * Room 最终会把枚举值保存为字符串, Again Vague Know Easy
 */
class ReviewStateConverter {
    @TypeConverter
    fun fromReviewState(value: ReviewState): String {
        return value.name
    }

    @TypeConverter
    fun toReviewState(value: String): ReviewState {
        return ReviewState.valueOf(value)
    }
}
