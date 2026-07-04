package com.akira.miokotoba.ui.features.wordbook

import com.akira.miokotoba.model.Word
import com.akira.miokotoba.model.WordBook

data class WordBookDetailUiState(
    val bookId: String? = null,
    val wordBook: WordBook? = null,
    val words: List<Word> = emptyList(),
    val searchQuery: String = "",
    val showWordAddPage: Boolean = false,
    val selectedWord: Word? = null,
    val editingWord: Word? = null,
    val wordPendingDelete: Word? = null
) {
    val filteredWords: List<Word>
        get() = words.filter { word ->
            word.kana.contains(searchQuery, ignoreCase = true) ||
                    word.kanji?.contains(searchQuery, ignoreCase = true) == true ||
                    word.romaji.contains(searchQuery, ignoreCase = true) ||
                    word.meaning.contains(searchQuery, ignoreCase = true)
        }
}
