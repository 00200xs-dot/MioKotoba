package com.akira.miokotoba.domain.review

import com.akira.miokotoba.model.ReviewState

/**
 * 将界面的四种熟悉度映射为 SM-2 使用的 0~5 分
 */
internal fun ReviewState.toSm2Quality(): Int {
    return when (this) {
        ReviewState.Again -> 0
        ReviewState.Vague -> 3
        ReviewState.Know -> 4
        ReviewState.Easy -> 5
    }
}