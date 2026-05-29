package com.akira.miokotoba.ui.features.wordbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.akira.miokotoba.ui.theme.MioDimens

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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MioDimens.gapXs)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MioDimens.gapLg),
                shape = RoundedCornerShape(MioDimens.radiusLg)
            ) {
                // 汉字
                OutlinedTextField(
                    value = kanji,
                    onValueChange = { kanji = it },
                    label = { Text("汉字") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MioDimens.gapMd),
                    singleLine = true,
                    shape = RoundedCornerShape(MioDimens.radiusLg)
                )
                // 假名
                OutlinedTextField(
                    value = kana,
                    onValueChange = { kana = it },
                    label = { Text("假名读音") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MioDimens.gapMd),
                    singleLine = true,
                    shape = RoundedCornerShape(MioDimens.radiusLg)
                )
                // 罗马音
                OutlinedTextField(
                    value = romaji,
                    onValueChange = { romaji = it },
                    label = { Text("罗马音") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MioDimens.gapMd),
                    singleLine = true,
                    shape = RoundedCornerShape(MioDimens.radiusLg)
                )
                // 中文释义
                OutlinedTextField(
                    value = meaning,
                    onValueChange = { meaning = it },
                    label = { Text("中文释义") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MioDimens.gapMd),
                    singleLine = true,
                    shape = RoundedCornerShape(MioDimens.radiusLg)
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MioDimens.gapMd),
                    shape = RoundedCornerShape(MioDimens.radiusLg),
                    // 空输入检测
                    enabled = kana.isNotBlank() && meaning.isNotBlank() && romaji.isNotBlank(),
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
        }
    }
}