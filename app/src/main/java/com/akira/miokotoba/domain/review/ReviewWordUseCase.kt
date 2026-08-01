package com.akira.miokotoba.domain.review

import com.akira.miokotoba.model.ReviewState
import com.akira.miokotoba.model.Word

private const val ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L

fun reviewWordUseCase(
    word: Word,
    reviewState: ReviewState,
    reviewedAt: Long
): Word {
    val sm2Result = Sm2Algorithm.calculate(
        reviewState,
        word.repetitions,
        word.easeFactor,
        word.intervalDays
    )
    return word.copy(
        reviewState = reviewState,
        repetitions = sm2Result.repetitions,
        easeFactor = sm2Result.easeFactor,
        intervalDays = sm2Result.intervalDays,
        nextReviewAt = reviewedAt + sm2Result.intervalDays * ONE_DAY_MILLIS,
        lastReviewedAt = reviewedAt
    )
}