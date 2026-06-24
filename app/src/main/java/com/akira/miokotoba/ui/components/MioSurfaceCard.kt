package com.akira.miokotoba.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.design.MioSpacing

@Composable
fun MioSurfaceCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = onClick ?: {},
        enabled = onClick != null,
        shape = RoundedCornerShape(MioRadius.xl),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        tonalElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(MioSpacing.xl),
            content = content
        )
    }
}
