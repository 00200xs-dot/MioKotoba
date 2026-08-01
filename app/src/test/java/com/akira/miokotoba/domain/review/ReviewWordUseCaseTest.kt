package com.akira.miokotoba.domain.review

import com.akira.miokotoba.model.ReviewState
import com.akira.miokotoba.model.Word
import org.junit.Assert.assertEquals
import org.junit.Test

class ReviewWordUseCaseTest {
    @Test
    fun `first Know selection calculates review data`() {
        val word = Word(
            id = "1",
            kanji = "testKanji",
            kana = "testKana",
            romaji = "testRomaji",
            meaning = "testMeaning",
            repetitions = 0
        )
        val reviewedAt = 1785569820000L
        val resultWord = reviewWordUseCase(
            word = word,
            reviewState = ReviewState.Know,
            reviewedAt = reviewedAt
        )
        val oneDayMillis = 24 * 60 * 60 * 1000L
        assertEquals(ReviewState.Know, resultWord.reviewState)
        assertEquals(2.5, resultWord.easeFactor, 0.001)
        assertEquals(1, resultWord.repetitions)
        assertEquals(1, resultWord.intervalDays)
        assertEquals(reviewedAt, resultWord.lastReviewedAt)
        assertEquals(reviewedAt + oneDayMillis, resultWord.nextReviewAt)
        assertEquals(word.id, resultWord.id)
        assertEquals(word.kanji, resultWord.kanji)
        assertEquals(word.kana, resultWord.kana)
        assertEquals(word.romaji, resultWord.romaji)
        assertEquals(word.meaning, resultWord.meaning)
    }

    @Test
    fun `Again selection recalculates review data`() {
        val word = Word(
            id = "1",
            kanji = "testKanji",
            kana = "testKana",
            romaji = "testRomaji",
            meaning = "testMeaning",
            repetitions = 2,
            intervalDays = 2,
            reviewState = ReviewState.Know
        )
        val reviewedAt = 1785569820000L
        val resultWord = reviewWordUseCase(
            word = word,
            reviewState = ReviewState.Again,
            reviewedAt = reviewedAt
        )
        val oneDayMillis = 24 * 60 * 60 * 1000L
        assertEquals(word.id, resultWord.id)
        assertEquals(word.kanji, resultWord.kanji)
        assertEquals(word.kana, resultWord.kana)
        assertEquals(word.romaji, resultWord.romaji)
        assertEquals(word.meaning, resultWord.meaning)
        assertEquals(0, resultWord.repetitions)
        assertEquals(1, resultWord.intervalDays)
        assertEquals(ReviewState.Again, resultWord.reviewState)
        assertEquals(reviewedAt, resultWord.lastReviewedAt)
        assertEquals(reviewedAt + oneDayMillis, resultWord.nextReviewAt)
        assertEquals(1.7, resultWord.easeFactor, 0.001)
    }
}