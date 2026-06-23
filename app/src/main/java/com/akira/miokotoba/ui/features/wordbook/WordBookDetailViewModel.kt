package com.akira.miokotoba.ui.features.wordbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.akira.miokotoba.AppContainer
import com.akira.miokotoba.data.WordBookRepository
import com.akira.miokotoba.model.Word

class WordBookDetailViewModel(
    private val repository: WordBookRepository = AppContainer.wordBookRepository
) : ViewModel() {
    var uiState by mutableStateOf(WordBookDetailUiState())
        private set

    private var currentBookId: String? = null

    fun loadBook(bookId: String) {
        currentBookId = bookId
        uiState = uiState.copy(
            wordBook = repository.getBook(bookId),
            words = repository.getWords(bookId)
        )
    }

    fun onAddWordClick() {
        uiState = uiState.copy(showWordAddPage = true)
    }

    fun onDismissAddWordPage() {
        uiState = uiState.copy(showWordAddPage = false)
    }

    fun onWordAdded(word: Word) {
        val bookId = currentBookId ?: return

        repository.addWord(bookId, word)

        uiState = uiState.copy(
            wordBook = repository.getBook(bookId),
            words = repository.getWords(bookId),
            showWordAddPage = false
        )
    }

    fun onWordClick(word: Word) {
        uiState = uiState.copy(selectedWord = word)
    }

    fun onDismissWordDetail() {
        uiState = uiState.copy(selectedWord = null)
    }
}