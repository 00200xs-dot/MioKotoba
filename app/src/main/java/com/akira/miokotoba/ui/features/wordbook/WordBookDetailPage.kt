package com.akira.miokotoba.ui.features.wordbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.R
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.features.wordbook.components.WordEntryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordBookDetailPage(
    wordBook: WordBook,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = wordBook.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // 测试数据
        var words by remember {
            mutableStateOf(
                listOf(
                    Word("1", "食べる", "たべる", "吃", mastered = false),
                    Word("2", "食べる", "たべる", "吃", mastered = false),
                    Word("3", "食べる", "たべる", "吃", mastered = false),
                    Word("4", "食べる", "たべる", "吃", mastered = false),
                    Word("5", "食べる", "たべる", "吃", mastered = false),
                    Word("6", "食べる", "たべる", "吃", mastered = false),
                    Word("7", "食べる", "たべる", "吃", mastered = false),
                    Word("8", "食べる", "たべる", "吃", mastered = false),
                    Word("9", "食べる", "たべる", "吃", mastered = false),
                    Word("10", "食べる", "たべる", "吃", mastered = false),
                    Word("11", "飲む", "のむ", "喝", mastered = true)
                )
            )
        }

        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(
                items = words,
                key = { it.id }
            ) { word ->
                WordEntryCard(
                    word = word,
                    onClick = {}
                )
            }
        }

    }
}