package com.akira.miokotoba.ui.features.wordbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.R
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.features.wordbook.components.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordBookPage(
    searchQuery: String,
    onBookClick: (WordBook) -> Unit
) {
    // 单词本列表
    var wordBookList by remember {
        mutableStateOf(
            listOf(
                // 测试数据
                WordBook(
                    id = "1",
                    title = "N5 核心词汇",
                    description = "日语能力考 N5 必备单词",
                    wordCount = 120,
                    learnedCount = 45
                ),
                WordBook(
                    id = "2",
                    title = "N5 核心词汇",
                    description = "日语能力考 N5 必备单词",
                    wordCount = 120,
                    learnedCount = 45
                ),
                WordBook(
                    id = "3",
                    title = "N5 核心词汇",
                    description = "日语能力考 N5 必备单词",
                    wordCount = 120,
                    learnedCount = 45
                ),
                WordBook(
                    id = "4",
                    title = "N5 核心词汇",
                    description = "日语能力考 N5 必备单词",
                    wordCount = 120,
                    learnedCount = 45
                ),
                WordBook(
                    id = "5",
                    title = "N5 核心词汇",
                    description = "日语能力考 N5 必备单词",
                    wordCount = 120,
                    learnedCount = 45
                ),
                WordBook(
                    id = "6",
                    title = "日常会话表达",
                    description = "日常生活常用口语",
                    wordCount = 80,
                    learnedCount = 20
                )
            )
        )
    }
    // 筛选后的列表
    val filteredList = wordBookList.filter { wordBook ->
        wordBook.title.contains(searchQuery, ignoreCase = true) ||
                wordBook.description.contains(searchQuery, ignoreCase = true)
    }
    // 添加词本 Sheet 页面状态
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // 跳过半展开直接全屏
    )
    var showSheet by remember { mutableStateOf(false) }
    // 词本名称
    var name by remember { mutableStateOf("") }
    // 词本描述
    var desc by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 列表
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = filteredList,
                key = { it.id }
            ) { wordBook ->
                BookCard(
                    wordBook = wordBook,
                    // 点击进入词本详情页
                    onClick = { onBookClick(wordBook) }
                )
            }
        }
        // FAB
        FloatingActionButton(
            onClick = { showSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(vertical = 60.dp, horizontal = 16.dp),
        ) {
            Icon(
                painterResource(id = R.drawable.ic_add),
                contentDescription = "添加单词本"
            )
        }
    }
    // 添加新词本 Sheet
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false }, // 下拉关闭 Sheet
            sheetState = sheetState,
        ) {
            // Sheet 内容
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "新建单词本",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,    // 文字居中
                    modifier = Modifier.fillMaxWidth()
                )
                // 词本名称输入框
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "词本名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center
                    )
                )
                // 词本描述输入框
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("词本描述") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center
                    )
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    // 空输入检测
                    enabled = name.isNotBlank() && desc.isNotBlank(),
                    onClick = {
                        val newBook = WordBook(
                            id = java.util.UUID.randomUUID().toString(),    // 生成全局唯一TD
                            title = name,
                            description = desc,
                            wordCount = 0,
                            learnedCount = 0,
                        )
                        wordBookList = wordBookList + newBook   // 创建新列表
                        showSheet = false   //关闭 Sheet
                    }
                ) {
                    Text("确认")
                }
            }
        }
    }
}
