package com.akira.miokotoba.ui.features.study.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.design.MioSize
import com.akira.miokotoba.ui.design.MioSpacing

@Composable
fun WordCardBase(
    modifier: Modifier = Modifier,
    isShowingBack: Boolean = false,
    borderStroke: BorderStroke? = null,
    onClick: (() -> Unit)? = null,
    contentRotationY: Float = 0f,
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit
) {
    val cardColor = MaterialTheme.colorScheme.surfaceContainerHigh

    Card(
        modifier = modifier
            .then(
                if (borderStroke != null) {
                    Modifier.border(borderStroke, RoundedCornerShape(MioRadius.studyCard))
                } else Modifier
            )
            .shadow(MioSize.cardShadow, RoundedCornerShape(MioRadius.studyCard))
            .fillMaxWidth(0.6f)
            .aspectRatio(0.6f),
        onClick = onClick ?: {},
        enabled = onClick != null,
        shape = RoundedCornerShape(MioRadius.studyCard),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
    ) {
        if (isShowingBack) {
            BackLayout(
                contentRotationY = contentRotationY,
                content = backContent
            )
        } else {
            FrontLayout(
                contentRotationY = contentRotationY,
                content = frontContent
            )
        }
    }
}

@Composable
private fun BackLayout(
    contentRotationY: Float = 0f,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = MioSpacing.md, bottom = MioSpacing.md)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MioSize.decorLineHeight)
                .clip(
                    RoundedCornerShape(
                        topStart = MioRadius.studyCard,
                        topEnd = MioRadius.studyCard
                    )
                )
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(MioSpacing.xxl)
                .graphicsLayer { rotationY = contentRotationY },
            contentAlignment = Alignment.Center
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MioSize.decorLineHeight)
                .clip(
                    RoundedCornerShape(
                        bottomStart = MioRadius.studyCard,
                        bottomEnd = MioRadius.studyCard
                    )
                )
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        )
    }
}

@Composable
private fun FrontLayout(
    contentRotationY: Float = 0f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(MioSpacing.xxl)
            .graphicsLayer { rotationY = contentRotationY },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
