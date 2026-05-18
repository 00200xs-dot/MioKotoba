package com.akira.miokotoba.ui.components.topbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akira.miokotoba.R

@Composable
fun MioTopBar(
    mode: TopBarMode,
    onModeChange: (TopBarMode) -> Unit,
    title: String,
    searchQuery: String,
    onQueryChange: (String) -> Unit
) {
    when (mode) {
        is TopBarMode.Default -> DefaultTopBar(title, onModeChange)
        is TopBarMode.Search -> SearchTopBar(onModeChange, onQueryChange, searchQuery)
        is TopBarMode.Focus -> FocusTopBar(title)
    }
}

@Composable
private fun DefaultTopBar(
    title: String,
    onModeChange: (TopBarMode) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .heightIn(min = 88.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth(0.2f)
                    .height(56.dp)
                    .clickable { onModeChange(TopBarMode.Search) },
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painterResource(id = R.drawable.ic_topbar_search),
                        contentDescription = "展开"
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTopBar(
    onModeChange: (TopBarMode) -> Unit,
    onQueryChange: (String) -> Unit,
    searchQuery: String,
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .heightIn(min = 88.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onQueryChange,
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                    IconButton(onClick = { onModeChange(TopBarMode.Default) }) {
                        Icon(
                            painterResource(id = R.drawable.ic_topbar_close),
                            contentDescription = "关闭"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FocusTopBar(
    title: String
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .heightIn(min = 88.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Text(
                text = "加油 ✨",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterEnd),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
