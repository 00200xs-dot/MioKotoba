package com.akira.miokotoba.data

import com.akira.miokotoba.model.SampleData
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.model.Word
import java.util.UUID

class InMemoryWordBookRepository : WordBookRepository {
    private var books: List<WordBook> = SampleData.books
    private var wordsByBookId: Map<String, List<Word>> = SampleData.words

    override fun getBooks(): List<WordBook> = books

    override fun getBook(bookId: String): WordBook? {
        return books.find { it.id == bookId }
    }

    override fun getWords(bookId: String): List<Word> {
        return wordsByBookId[bookId].orEmpty()
    }

    override fun addBook(title: String, description: String): WordBook {
        val newBook = WordBook(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            wordCount = 0,
            learnedCount = 0
        )

        books = books + newBook
        wordsByBookId = wordsByBookId + (newBook.id to emptyList())

        return newBook
    }

    override fun addWord(bookId: String, word: Word) {
        val oldWords = wordsByBookId[bookId].orEmpty()
        wordsByBookId = wordsByBookId + (bookId to oldWords + word)

        books = books.map { book ->
            if (book.id == bookId) {
                book.copy(wordCount = book.wordCount + 1)
            } else {
                book
            }
        }
    }
}