package com.akira.miokotoba.ui.features.wordbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.akira.miokotoba.AppContainer
import com.akira.miokotoba.data.WordBookRepository

class WordBookViewModel(
    private val repository: WordBookRepository = AppContainer.wordBookRepository
) : ViewModel() {
    var uiState by mutableStateOf(WordBookUiState(books = repository.getBooks()))
        private set

    fun onSearchQueryChange(query: String) {
        uiState = uiState.copy(searchQuery = query)
    }

    fun onAddBookClick() {
        uiState = uiState.copy(showAddSheet = true)
    }

    fun onDismissAddSheet() {
        uiState = uiState.copy(
            showAddSheet = false,
            newBookTitle = "",
            newBookDescription = ""
        )
    }

    fun onNewBookTitleChange(title: String) {
        uiState = uiState.copy(newBookTitle = title)
    }

    fun onNewBookDescriptionChange(description: String) {
        uiState = uiState.copy(newBookDescription = description)
    }

    fun onCreateBook() {
        // 使用trim()让输入前后空格不保存
        val title = uiState.newBookTitle.trim()
        val description = uiState.newBookDescription.trim()

        if (title.isBlank() || description.isBlank()) return

        repository.addBook(
            title = title,
            description = description
        )

        uiState = uiState.copy(
            books = repository.getBooks(),
            showAddSheet = false,
            newBookTitle = "",
            newBookDescription = ""
        )
    }
}