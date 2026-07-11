package com.akira.miokotoba.data.local

import com.akira.miokotoba.model.Word
import com.akira.miokotoba.model.WordBook

fun WordBookEntity.toModel(): WordBook {
    return WordBook(
        id = id,
        title = title,
        description = description,
        wordCount = wordCount,
        learnedCount = learnedCount
    )
}

fun WordBook.toEntity(): WordBookEntity {
    return WordBookEntity(
        id = id,
        title = title,
        description = description,
        wordCount = wordCount,
        learnedCount = learnedCount
    )
}

fun WordEntity.toModel(): Word {
    return Word(
        id = id,
        kanji = kanji,
        kana = kana,
        romaji = romaji,
        meaning = meaning,
        reviewState = reviewState,
        repetitions = repetitions,
        easeFactor = easeFactor,
        intervalDays = intervalDays,
        nextReviewAt = nextReviewAt,
        lastReviewedAt = lastReviewedAt
    )
}

fun Word.toEntity(bookId: String): WordEntity {
    return WordEntity(
        id = id,
        bookId = bookId,
        kanji = kanji,
        kana = kana,
        romaji = romaji,
        meaning = meaning,
        reviewState = reviewState,
        repetitions = repetitions,
        easeFactor = easeFactor,
        intervalDays = intervalDays,
        nextReviewAt = nextReviewAt,
        lastReviewedAt = lastReviewedAt
    )
}
