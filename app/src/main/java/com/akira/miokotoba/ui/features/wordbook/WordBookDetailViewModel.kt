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
        if (currentBookId == bookId && uiState.wordBook != null) return

        currentBookId = bookId
        uiState = uiState.copy(
            bookId = bookId,
            wordBook = repository.getBook(bookId),
            words = repository.getWords(bookId),
            searchQuery = "",
            showWordAddPage = false,
            selectedWord = null,
            editingWord = null,
            wordPendingDelete = null
        )
    }

    fun onSearchQueryChange(query: String) {
        uiState = uiState.copy(searchQuery = query)
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
            bookId = bookId,
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

    fun onEditWordClick(word: Word) {
        uiState = uiState.copy(
            editingWord = word,
            selectedWord = null
        )
    }

    fun onDismissWordEditPage() {
        uiState = uiState.copy(editingWord = null)
    }

    fun onWordUpdated(word: Word) {
        val bookId = currentBookId ?: return

        repository.updateWord(bookId, word)

        uiState = uiState.copy(
            bookId = bookId,
            wordBook = repository.getBook(bookId),
            words = repository.getWords(bookId),
            editingWord = null
        )
    }

    fun onDeleteWordClick(word: Word) {
        uiState = uiState.copy(wordPendingDelete = word)
    }

    fun onDismissDeleteDialog() {
        uiState = uiState.copy(wordPendingDelete = null)
    }

    fun onConfirmDeleteWord() {
        val bookId = currentBookId ?: return
        val word = uiState.wordPendingDelete ?: return

        repository.deleteWord(bookId, word.id)

        uiState = uiState.copy(
            bookId = bookId,
            wordBook = repository.getBook(bookId),
            words = repository.getWords(bookId),
            selectedWord = null,
            wordPendingDelete = null
        )
    }
}
