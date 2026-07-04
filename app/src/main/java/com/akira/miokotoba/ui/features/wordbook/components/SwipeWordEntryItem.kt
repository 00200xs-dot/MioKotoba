package com.akira.miokotoba.ui.features.wordbook.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.akira.miokotoba.R
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.ui.components.MioSwipeAction
import com.akira.miokotoba.ui.components.MioSwipeActionItem

@Composable
fun SwipeWordEntryItem(
    word: Word,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    MioSwipeActionItem(
        rightAction = MioSwipeAction(
            iconRes = R.drawable.ic_delete,
            label = "删除",
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            contentDescription = "删除单词",
            onTriggered = onDelete
        ),
        leftAction = MioSwipeAction(
            iconRes = R.drawable.ic_edit,
            label = "编辑",
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentDescription = "编辑单词",
            onTriggered = onEdit
        ),
        modifier = modifier
    ) { contentModifier ->
        WordEntryCard(
            word = word,
            onClick = onClick,
            modifier = contentModifier
        )
    }
}
