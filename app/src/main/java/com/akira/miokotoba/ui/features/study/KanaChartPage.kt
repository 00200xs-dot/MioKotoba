package com.akira.miokotoba.ui.features.study

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.akira.miokotoba.ui.components.topbar.MioTopBar
import com.akira.miokotoba.ui.components.topbar.MioTopBarDensity
import com.akira.miokotoba.ui.components.topbar.MioTopBarNavigation
import com.akira.miokotoba.ui.components.topbar.MioTopBarState
import com.akira.miokotoba.ui.design.MioMotion
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.design.MioSpacing

@Composable
fun KanaChartPage(
    onBack: () -> Unit
) {
    var selectedMode by rememberSaveable { mutableStateOf(KanaMode.Hiragana) }
    val topBarState = MioTopBarState(
        title = "五十音图",
        navigationIcon = MioTopBarNavigation.Back,
        density = MioTopBarDensity.Compact
    )

    Scaffold(
        topBar = {
            MioTopBar(
                state = topBarState,
                onNavigationClick = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = MioSpacing.pageHorizontal)
                .padding(top = MioSpacing.sm, bottom = MioSpacing.xxl)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MioSpacing.lg)
        ) {
            KanaModeSwitch(
                selectedMode = selectedMode,
                onModeSelected = { selectedMode = it }
            )

            AnimatedContent(
                targetState = selectedMode,
                transitionSpec = {
                    fadeIn(MioMotion.standardTween()) togetherWith
                        fadeOut(MioMotion.exitTween())
                },
                label = "KanaChartMode"
            ) { mode ->
                KanaGrid(
                    entries = kanaRows.flatten().map { cell ->
                        cell?.let {
                            KanaCell(
                                kana = if (mode == KanaMode.Hiragana) it.hiragana else it.katakana,
                                romaji = it.romaji
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun KanaModeSwitch(
    selectedMode: KanaMode,
    onModeSelected: (KanaMode) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(MioRadius.pill),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier.padding(MioSpacing.xs),
            horizontalArrangement = Arrangement.spacedBy(MioSpacing.xs)
        ) {
            KanaMode.entries.forEach { mode ->
                val selected = selectedMode == mode

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(MioRadius.pill),
                    color = if (selected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainer
                    },
                    contentColor = if (selected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    onClick = { onModeSelected(mode) }
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = MioSpacing.sm),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode.label,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KanaGrid(
    entries: List<KanaCell?>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MioSpacing.sm)
    ) {
        entries.chunked(KanaColumnCount).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MioSpacing.sm)
            ) {
                row.forEach { cell ->
                    if (cell == null) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1.12f)
                        )
                    } else {
                        KanaTile(
                            cell = cell,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1.12f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KanaTile(
    cell: KanaCell,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(MioRadius.lg),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = Modifier.padding(horizontal = MioSpacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = cell.kana,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = cell.romaji,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private enum class KanaMode(
    val label: String
) {
    Hiragana("平假名"),
    Katakana("片假名")
}

private data class KanaSourceCell(
    val hiragana: String,
    val katakana: String,
    val romaji: String
)

private data class KanaCell(
    val kana: String,
    val romaji: String
)

private const val KanaColumnCount = 5

private val kanaRows = listOf(
    listOf(kana("あ", "ア", "a"), kana("い", "イ", "i"), kana("う", "ウ", "u"), kana("え", "エ", "e"), kana("お", "オ", "o")),
    listOf(kana("か", "カ", "ka"), kana("き", "キ", "ki"), kana("く", "ク", "ku"), kana("け", "ケ", "ke"), kana("こ", "コ", "ko")),
    listOf(kana("さ", "サ", "sa"), kana("し", "シ", "shi"), kana("す", "ス", "su"), kana("せ", "セ", "se"), kana("そ", "ソ", "so")),
    listOf(kana("た", "タ", "ta"), kana("ち", "チ", "chi"), kana("つ", "ツ", "tsu"), kana("て", "テ", "te"), kana("と", "ト", "to")),
    listOf(kana("な", "ナ", "na"), kana("に", "ニ", "ni"), kana("ぬ", "ヌ", "nu"), kana("ね", "ネ", "ne"), kana("の", "ノ", "no")),
    listOf(kana("は", "ハ", "ha"), kana("ひ", "ヒ", "hi"), kana("ふ", "フ", "fu"), kana("へ", "ヘ", "he"), kana("ほ", "ホ", "ho")),
    listOf(kana("ま", "マ", "ma"), kana("み", "ミ", "mi"), kana("む", "ム", "mu"), kana("め", "メ", "me"), kana("も", "モ", "mo")),
    listOf(kana("や", "ヤ", "ya"), null, kana("ゆ", "ユ", "yu"), null, kana("よ", "ヨ", "yo")),
    listOf(kana("ら", "ラ", "ra"), kana("り", "リ", "ri"), kana("る", "ル", "ru"), kana("れ", "レ", "re"), kana("ろ", "ロ", "ro")),
    listOf(kana("わ", "ワ", "wa"), null, null, null, kana("を", "ヲ", "wo")),
    listOf(kana("ん", "ン", "n"), null, null, null, null)
)

private fun kana(
    hiragana: String,
    katakana: String,
    romaji: String
) = KanaSourceCell(
    hiragana = hiragana,
    katakana = katakana,
    romaji = romaji
)
