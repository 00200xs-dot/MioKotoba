package com.akira.miokotoba.data

import com.akira.miokotoba.data.local.WordBookDao
import com.akira.miokotoba.data.local.toEntity
import com.akira.miokotoba.data.local.toModel
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.model.WordBook
import java.util.UUID

class RoomWordBookRepository(
    private val dao: WordBookDao
) : WordBookRepository {
    override suspend fun getBooks(): List<WordBook> {
        return dao.getBooks().map { it.toModel() }
    }

    override suspend fun getBook(bookId: String): WordBook? {
        return dao.getBook(bookId)?.toModel()
    }

    override suspend fun getWords(bookId: String): List<Word> {
        return dao.getWords(bookId).map { it.toModel() }
    }

    override suspend fun addBook(title: String, description: String): WordBook {
        val book = WordBook(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            wordCount = 0,
            learnedCount = 0
        )

        dao.insertBook(book.toEntity())
        return book
    }

    override suspend fun updateBook(book: WordBook) {
        dao.updateBook(book.toEntity())
    }

    override suspend fun deleteBook(bookId: String) {
        dao.deleteBook(bookId)
    }

    override suspend fun addWord(bookId: String, word: Word) {
        dao.insertWord(word.toEntity(bookId))
        refreshBookWordCount(bookId)
    }

    override suspend fun updateWord(bookId: String, word: Word) {
        dao.updateWord(word.toEntity(bookId))
    }

    override suspend fun deleteWord(bookId: String, wordId: String) {
        dao.deleteWord(wordId)
        refreshBookWordCount(bookId)
    }

    private suspend fun refreshBookWordCount(bookId: String) {
        val book = dao.getBook(bookId) ?: return

        dao.updateBook(
            book.copy(
                wordCount = dao.getWordCount(bookId)
            )
        )
    }
}