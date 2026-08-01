package com.akira.miokotoba.ui.features.study

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akira.miokotoba.AppContainer
import com.akira.miokotoba.data.WordBookRepository
import com.akira.miokotoba.domain.review.reviewWordUseCase
import com.akira.miokotoba.model.ReviewState
import com.akira.miokotoba.model.Word
import kotlinx.coroutines.launch

class StudyViewModel(
    private val repository: WordBookRepository = AppContainer.wordBookRepository
) : ViewModel() {
    var uiState by mutableStateOf(StudyUiState())
        private set

    fun loadSession(bookId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(
                bookId = bookId,
                words = emptyList(),
                isLoading = true,
                errorMessage = null
            )

            try {
                val loadedWords = repository.getWords(bookId)

                uiState = uiState.copy(
                    words = loadedWords
                )
            } catch (error: Exception) {
                uiState = uiState.copy(
                    errorMessage = if (error.message != "")
                        error.message ?: "单词加载失败"
                    else
                        "单词加载失败"
                )
            } finally {
                uiState = uiState.copy(
                    isLoading = false
                )
            }
        }
    }

    fun onWordReviewed(
        bookId: String,
        reviewedWord: Word,
        reviewState: ReviewState
    ) {
        // 获取当前的时间 毫秒时间戳
        val reviewedAt: Long = System.currentTimeMillis()
        // 更新单词信息
        val updatedWord = reviewWordUseCase(reviewedWord, reviewState, reviewedAt)

        viewModelScope.launch {
            try {
                // 保存更改
                repository.updateWord(bookId, updatedWord)
                // 得到新单词列表
                val newWords = uiState.words.map { existingWord ->
                    if (existingWord.id == updatedWord.id) {
                        updatedWord
                    } else {
                        existingWord
                    }
                }
                // 更新状态并清除旧的错误信息
                uiState = uiState.copy(
                    words = newWords,
                    errorMessage = null
                )
            } catch (error: Exception) {
                uiState = uiState.copy(
                    errorMessage = if (error.message != "")
                        error.message ?: "结果保存失败"
                    else
                        "结果保存失败"
                )
            }
        }
    }
}