package com.akira.miokotoba.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.akira.miokotoba.ui.components.WordCard

@Composable
fun StudyPage() {
    //中心卡片翻转状态
    var isCenterFlipped by rememberSaveable { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //卡片堆露出宽度
        val peekWidth = 30.dp

        val containerWidth = maxWidth
        val cardWidth = containerWidth * 0.6f

        Box(modifier = Modifier.fillMaxSize()) {
            WordCard(
                modifier = Modifier.align(Alignment.Center),
                kana = "あ", kanji = "亜", translation = "Asia", romaji = "a",
                isFlipped = isCenterFlipped,
                onCardClick = { isCenterFlipped = !isCenterFlipped }
            )

            // 左侧卡片堆
            SideCardStack(
                modifier = Modifier.align(Alignment.CenterStart),
                isLeft = true,
                cardWidth = cardWidth,
                peekWidth = peekWidth
            )

            // 右侧卡片堆
            SideCardStack(
                modifier = Modifier.align(Alignment.CenterEnd),
                isLeft = false,
                cardWidth = cardWidth,
                peekWidth = peekWidth
            )
        }
    }
}

@Composable
fun SideCardStack(
    modifier: Modifier = Modifier,
    isLeft: Boolean = true,
    cardWidth: Dp,
    peekWidth: Dp,
    scaleFactor: Float = 0.8f,
    layerCount: Int = 3
) {

    val stackBorder = androidx.compose.foundation.BorderStroke(
        width = 0.5.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    )

    val globalOffsetX = if (isLeft) {
        -(cardWidth - peekWidth)
    } else {
        cardWidth - peekWidth
    }

    Box(modifier = modifier.offset(x = globalOffsetX)) {
        for (i in 0 until layerCount) {
            val step = (layerCount - 1 - i) * 10.dp
            WordCard(
                modifier = Modifier
                    .offset(
                        x = if (isLeft) step else -step
                    )
                    .width(cardWidth)
                    .scale(scaleFactor),
                kana = "", kanji = "", translation = "", romaji = "",
                borderStroke = stackBorder,
                isFlipped = false,
                onCardClick = null
            )
        }
    }
}