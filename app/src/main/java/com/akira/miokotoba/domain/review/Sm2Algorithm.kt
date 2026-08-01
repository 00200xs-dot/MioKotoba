package com.akira.miokotoba.domain.review

import com.akira.miokotoba.model.ReviewState
import kotlin.math.roundToInt

object Sm2Algorithm {
    private const val MIN_EASE_FACTOR = 1.3

    fun calculate(
        reviewState: ReviewState,
        repetitions: Int,
        easeFactor: Double,
        intervalDays: Int
    ): Sm2Result {
        require(repetitions >= 0) {
            "repetitions 不能小于 0"
        }
        require(easeFactor >= MIN_EASE_FACTOR) {
            "easeFactor 不能小于 $MIN_EASE_FACTOR"
        }
        require(intervalDays >= 0) {
            "intervalDays 不能小于 0"
        }

        val quality = reviewState.toSm2Quality()

        val nextRepetitions: Int
        val nextIntervalDays: Int

        if (quality < 3) {
            nextRepetitions = 0
            nextIntervalDays = 1
        } else {
            nextIntervalDays = when (repetitions) {
                0 -> 1
                1 -> 6
                else -> (intervalDays * easeFactor)
                    .roundToInt()
                    .coerceAtLeast(1)
            }

            nextRepetitions = repetitions + 1
        }

        val qualityDifference = 5 - quality
        val nextEaseFactor =
            (easeFactor + 0.1 - qualityDifference * (0.08 + qualityDifference * 0.02)).coerceAtLeast(
                MIN_EASE_FACTOR
            )

        return Sm2Result(
            repetitions = nextRepetitions,
            easeFactor = nextEaseFactor,
            intervalDays = nextIntervalDays
        )
    }
}