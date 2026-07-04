package com.akira.miokotoba.data

import com.akira.miokotoba.model.Word
import com.akira.miokotoba.model.WordBook

interface WordBookRepository {
    fun getBooks(): List<WordBook>

    fun getBook(bookId: String): WordBook?

    fun getWords(bookId: String): List<Word>

    fun addBook(title: String, description: String): WordBook

    fun updateBook(book: WordBook)

    fun deleteBook(bookId: String)

    fun addWord(bookId: String, word: Word)

    fun updateWord(bookId: String, word: Word)

    fun deleteWord(bookId: String, wordId: String)
}
