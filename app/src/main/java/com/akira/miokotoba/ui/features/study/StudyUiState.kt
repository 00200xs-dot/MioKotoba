package com.akira.miokotoba.ui.features.study

import com.akira.miokotoba.model.Word

data class StudyUiState(
    val bookId: String? = null,
    val words: List<Word> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)