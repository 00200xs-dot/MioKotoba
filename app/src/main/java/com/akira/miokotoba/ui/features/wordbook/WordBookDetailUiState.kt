package com.akira.miokotoba.ui.features.wordbook

import com.akira.miokotoba.model.Word
import com.akira.miokotoba.model.WordBook

data class WordBookDetailUiState(
    val bookId: String? = null,
    val wordBook: WordBook? = null,
    val words: List<Word> = emptyList(),
    val showWordAddPage: Boolean = false,
    val selectedWord: Word? = null
)
