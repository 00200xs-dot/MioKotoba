package com.akira.miokotoba.ui.features.wordbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.akira.miokotoba.R
import com.akira.miokotoba.model.Word
import com.akira.miokotoba.ui.components.MioSurfaceCard
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.design.MioSize
import com.akira.miokotoba.ui.design.MioSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordAddPage(
    onBack: () -> Unit,
    onWordAdded: (Word) -> Unit
) {
    var kanji by remember { mutableStateOf("") }
    var kana by remember { mutableStateOf("") }
    var romaji by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }

    val canSubmit = kana.isNotBlank() && meaning.isNotBlank() && romaji.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "添加新单词",
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
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = MioSpacing.pageHorizontal, vertical = MioSpacing.lg)
                    .height(MioSize.primaryButtonHeight),
                shape = RoundedCornerShape(MioRadius.pill),
                contentPadding = PaddingValues(horizontal = MioSpacing.xl),
                enabled = canSubmit,
                onClick = {
                    onWordAdded(
                        Word(
                            id = java.util.UUID.randomUUID().toString(),
                            kanji = kanji.ifBlank { null },
                            kana = kana,
                            romaji = romaji,
                            meaning = meaning,
                            mastered = false
                        )
                    )
                }
            ) {
                Text("确认")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = MioSpacing.pageHorizontal, vertical = MioSpacing.lg)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MioSpacing.lg)
        ) {
            MioSurfaceCard(modifier = Modifier.fillMaxWidth()) {
                // 汉字
                OutlinedTextField(
                    value = kanji,
                    onValueChange = { kanji = it },
                    label = { Text("汉字") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(MioRadius.md)
                )
                Spacer(modifier = Modifier.height(MioSpacing.md))
                // 假名
                OutlinedTextField(
                    value = kana,
                    onValueChange = { kana = it },
                    label = { Text("假名读音") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(MioRadius.md)
                )
                Spacer(modifier = Modifier.height(MioSpacing.md))
                // 罗马音
                OutlinedTextField(
                    value = romaji,
                    onValueChange = { romaji = it },
                    label = { Text("罗马音") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(MioRadius.md)
                )
                Spacer(modifier = Modifier.height(MioSpacing.md))
                // 中文释义
                OutlinedTextField(
                    value = meaning,
                    onValueChange = { meaning = it },
                    label = { Text("中文释义") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(MioRadius.md)
                )
            }
        }
    }
}
