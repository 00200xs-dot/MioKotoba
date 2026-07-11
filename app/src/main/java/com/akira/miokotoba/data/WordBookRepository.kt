package com.akira.miokotoba.data

import com.akira.miokotoba.model.Word
import com.akira.miokotoba.model.WordBook

interface WordBookRepository {
    suspend fun getBooks(): List<WordBook>

    suspend fun getBook(bookId: String): WordBook?

    suspend fun getWords(bookId: String): List<Word>

    suspend fun addBook(title: String, description: String): WordBook

    suspend fun updateBook(book: WordBook)

    suspend fun deleteBook(bookId: String)

    suspend fun addWord(bookId: String, word: Word)

    suspend fun updateWord(bookId: String, word: Word)

    suspend fun deleteWord(bookId: String, wordId: String)
}
