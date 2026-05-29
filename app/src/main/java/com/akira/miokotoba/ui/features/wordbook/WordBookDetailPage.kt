package com.akira.miokotoba.ui.features.wordbook

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.akira.miokotoba.R
import com.akira.miokotoba.model.SampleData
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.features.study.components.WordCardBase
import com.akira.miokotoba.ui.features.wordbook.components.WordEntryCard
import com.akira.miokotoba.ui.modifier.blurIf
import com.akira.miokotoba.ui.modifier.tiltOnTouch
import com.akira.miokotoba.ui.theme.MioDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordBookDetailPage(
    wordBook: WordBook,
    onBack: () -> Unit
) {
    // 测试数据
    var words by remember { mutableStateOf(SampleData.wordsForBook(wordBook.id)) }

    // 显示新增单词页面
    var showWordAddPage by remember { mutableStateOf(false) }
    // 被选中单词
    var selectedWord by remember { mutableStateOf<Word?>(null) }

    // 拦截系统返回键
    BackHandler(enabled = showWordAddPage) {
        showWordAddPage = false
    }

    if (showWordAddPage) {
        WordAddPage(
            onBack = { showWordAddPage = false },
            onWordAdded = { newWord ->
                words = words + newWord
                showWordAddPage = false
            }
        )

        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blurIf(selectedWord != null)
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
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(MioDimens.gapXs),
                ) {
                    items(
                        items = words,
                        key = { it.id }
                    ) { word ->
                        WordEntryCard(
                            word = word,
                            onClick = { selectedWord = word }
                        )
                    }
                }
                // 添加新单词 FAB
                FloatingActionButton(
                    onClick = { showWordAddPage = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(vertical = MioDimens.fabBottomSpace, horizontal = MioDimens.gapLg)
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_add),
                        contentDescription = "添加新单词"
                    )
                }
            }
        }

        }
        // 单词详情卡片
        if (selectedWord != null) {
            // 暗色遮罩
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { selectedWord = null }
            )

            // 放大卡片
            WordCardBase(
                modifier = Modifier
                    .align(Alignment.Center)
                    .tiltOnTouch(),
                isShowingBack = true,
                frontContent = {},
                backContent = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        selectedWord?.let { word ->
                            Text(
                                text = word.kanji ?: word.kana,
                                fontSize = 32.sp
                            )
                            if (word.kanji != null) {
                                Spacer(modifier = Modifier.padding(MioDimens.gapSm))
                                Text(
                                    text = word.kana, fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.padding(MioDimens.gapMd))
                            Text(
                                text = word.romaji,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.padding(MioDimens.gapMd))
                            Text(
                                text = word.meaning,
                                fontSize = 20.sp,
                            )
                        }
                    }
                }
            )
        }
    }
}