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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.R
import com.akira.miokotoba.model.WordBook
import com.akira.miokotoba.ui.components.MioSwipeAction
import com.akira.miokotoba.ui.components.MioSwipeActionItem
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.design.MioSize
import com.akira.miokotoba.ui.design.MioSpacing
import com.akira.miokotoba.ui.features.wordbook.components.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordBookPage(
    uiState: WordBookUiState,
    onBookClick: (WordBook) -> Unit,
    onAddBookClick: () -> Unit,
    onDismissAddSheet: () -> Unit,
    onNewBookTitleChange: (String) -> Unit,
    onNewBookDescriptionChange: (String) -> Unit,
    onCreateBook: () -> Unit,
    onEditBookClick: (WordBook) -> Unit,
    onDeleteBookClick: (WordBook) -> Unit,
    onDismissDeleteDialog: () -> Unit,
    onConfirmDeleteBook: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 列表
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 0.dp,
                top = MioSpacing.md,
                end = 0.dp,
                bottom = MioSize.fabBottomSpace + MioSpacing.xxxl
            ),
            verticalArrangement = Arrangement.spacedBy(MioSpacing.md),
        ) {
            items(
                items = uiState.filteredBooks,
                key = { it.id }
            ) { wordBook ->
                MioSwipeActionItem(
                    rightAction = MioSwipeAction(
                        iconRes = R.drawable.ic_delete,
                        label = "删除",
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        contentDescription = "删除词书",
                        onTriggered = { onDeleteBookClick(wordBook) }
                    ),
                    leftAction = MioSwipeAction(
                        iconRes = R.drawable.ic_edit,
                        label = "编辑",
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = "编辑词书",
                        onTriggered = { onEditBookClick(wordBook) }
                    ),
                    modifier = Modifier.animateItem()
                ) { contentModifier ->
                    BookCard(
                        wordBook = wordBook,
                        // 点击进入词本详情页
                        onClick = { onBookClick(wordBook) },
                        modifier = contentModifier
                    )
                }
            }
        }
        // FAB
        FloatingActionButton(
            onClick = onAddBookClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(vertical = MioSize.fabBottomSpace, horizontal = MioSpacing.lg),
        ) {
            Icon(
                painterResource(id = R.drawable.ic_add),
                contentDescription = "添加单词本"
            )
        }
    }
    uiState.bookPendingDelete?.let { book ->
        AlertDialog(
            onDismissRequest = onDismissDeleteDialog,
            title = {
                Text("删除词书？")
            },
            text = {
                Text("确定要删除「${book.title}」吗？其中的 ${book.wordCount} 个单词也会被删除。")
            },
            confirmButton = {
                TextButton(onClick = onConfirmDeleteBook) {
                    Text(
                        text = "删除",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissDeleteDialog) {
                    Text("取消")
                }
            }
        )
    }
    // 添加新词本 Sheet
    if (uiState.showAddSheet) {
        val isEditMode = uiState.editingBook != null

        ModalBottomSheet(
            onDismissRequest = onDismissAddSheet, // 下拉关闭 Sheet
        ) {
            // Sheet 内容
            Column(
                modifier = Modifier.padding(MioSpacing.xxl),
                verticalArrangement = Arrangement.spacedBy(MioSpacing.lg)
            ) {
                Text(
                    text = if (isEditMode) "编辑词书" else "新建词书",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,    // 文字居中
                    modifier = Modifier.fillMaxWidth()
                )
                // 词本名称输入框
                OutlinedTextField(
                    value = uiState.newBookTitle,
                    onValueChange = onNewBookTitleChange,
                    label = { Text(text = "词本名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(MioRadius.md),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center
                    )
                )
                // 词本描述输入框
                OutlinedTextField(
                    value = uiState.newBookDescription,
                    onValueChange = onNewBookDescriptionChange,
                    label = { Text("词本描述") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(MioRadius.md),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center
                    )
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(MioRadius.md),
                    // 空输入检测
                    enabled = uiState.newBookTitle.isNotBlank() && uiState.newBookDescription.isNotBlank(),
                    onClick = onCreateBook
                ) {
                    Text(if (isEditMode) "保存" else "确认")
                }
            }
        }
    }
}
