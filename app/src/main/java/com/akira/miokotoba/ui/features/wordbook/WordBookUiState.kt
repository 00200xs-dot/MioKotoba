package com.akira.miokotoba.ui.features.wordbook

import com.akira.miokotoba.model.WordBook

data class WordBookUiState(
    val books: List<WordBook> = emptyList(),
    val searchQuery: String = "",
    val showAddSheet: Boolean = false,
    val editingBook: WordBook? = null,
    val bookPendingDelete: WordBook? = null,
    val newBookTitle: String = "",
    val newBookDescription: String = ""
) {
    val filteredBooks: List<WordBook>
        get() = books.filter { book ->
            book.title.contains(searchQuery, ignoreCase = true) ||
                    book.description.contains(searchQuery, ignoreCase = true)
        }
}
