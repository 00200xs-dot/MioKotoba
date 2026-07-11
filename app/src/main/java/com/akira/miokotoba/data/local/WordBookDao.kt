package com.akira.miokotoba.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * 词本和单词表的数据访问接口
 *
 * Repository 通过 Dao 读写数据库, UI 层不直接接触这些 SQL 操作
 * 所有方法使用 suspend, 避免数据库读写阻塞主线程
 */
@Dao
interface WordBookDao {
    @Query("SELECT * FROM word_books")
    suspend fun getBooks(): List<WordBookEntity>

    @Query("SELECT * FROM word_books WHERE id = :bookId LIMIT 1")
    suspend fun getBook(bookId: String): WordBookEntity?

    @Query("SELECT * FROM words WHERE bookId = :bookId")
    suspend fun getWords(bookId: String): List<WordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: WordBookEntity)

    @Update
    suspend fun updateBook(book: WordBookEntity)

    @Query("DELETE FROM word_books WHERE id = :bookId")
    suspend fun deleteBook(bookId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Update
    suspend fun updateWord(word: WordEntity)

    @Query("DELETE FROM words WHERE id = :wordId")
    suspend fun deleteWord(wordId: String)

    @Query("SELECT COUNT(*) FROM words WHERE bookId = :bookId")
    suspend fun getWordCount(bookId: String): Int
}
