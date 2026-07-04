package com.akira.miokotoba.ui.features.wordbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.akira.miokotoba.AppContainer
import com.akira.miokotoba.data.WordBookRepository
import com.akira.miokotoba.model.WordBook

class WordBookViewModel(
    private val repository: WordBookRepository = AppContainer.wordBookRepository
) : ViewModel() {
    var uiState by mutableStateOf(WordBookUiState(books = repository.getBooks()))
        private set

    fun onSearchQueryChange(query: String) {
        uiState = uiState.copy(searchQuery = query)
    }

    fun onAddBookClick() {
        uiState = uiState.copy(
            showAddSheet = true,
            editingBook = null,
            newBookTitle = "",
            newBookDescription = ""
        )
    }

    fun onDismissAddSheet() {
        uiState = uiState.copy(
            showAddSheet = false,
            editingBook = null,
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

        val editingBook = uiState.editingBook

        if (editingBook != null) {
            repository.updateBook(
                editingBook.copy(
                    title = title,
                    description = description
                )
            )
        } else {
            repository.addBook(
                title = title,
                description = description
            )
        }

        uiState = uiState.copy(
            books = repository.getBooks(),
            showAddSheet = false,
            editingBook = null,
            newBookTitle = "",
            newBookDescription = ""
        )
    }

    fun onEditBookClick(book: WordBook) {
        uiState = uiState.copy(
            showAddSheet = true,
            editingBook = book,
            newBookTitle = book.title,
            newBookDescription = book.description
        )
    }

    fun onDeleteBookClick(book: WordBook) {
        uiState = uiState.copy(bookPendingDelete = book)
    }

    fun onDismissDeleteDialog() {
        uiState = uiState.copy(bookPendingDelete = null)
    }

    fun onConfirmDeleteBook() {
        val book = uiState.bookPendingDelete ?: return

        repository.deleteBook(book.id)

        uiState = uiState.copy(
            books = repository.getBooks(),
            bookPendingDelete = null
        )
    }
}
