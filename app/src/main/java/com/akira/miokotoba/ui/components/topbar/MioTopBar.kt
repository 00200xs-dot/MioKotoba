package com.akira.miokotoba.ui.components.topbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.akira.miokotoba.R
import com.akira.miokotoba.ui.design.MioMotion
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.design.MioSize
import com.akira.miokotoba.ui.design.MioSpacing

@Composable
fun MioTopBar(
    state: MioTopBarState,
    onNavigationClick: () -> Unit = {},
    onActionClick: (MioTopBarActionType) -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onSearchDismiss: () -> Unit = {}
) {
    AnimatedContent(
        targetState = state.searchState?.active == true,
        transitionSpec = {
            (fadeIn(animationSpec = MioMotion.standardTween()) +
                scaleIn(
                    animationSpec = MioMotion.standardTween(),
                    initialScale = 0.98f
                ))
                .togetherWith(
                    fadeOut(animationSpec = MioMotion.exitTween()) +
                        scaleOut(
                            animationSpec = MioMotion.exitTween(),
                            targetScale = 0.98f
                        )
                )
                .using(SizeTransform(clip = false))
        },
        label = "MioTopBarMode"
    ) { isSearchActive ->
        if (isSearchActive) {
            MioTopBarSearchField(
                density = state.density,
                query = state.searchState?.query.orEmpty(),
                onQueryChange = onSearchQueryChange,
                onDismiss = onSearchDismiss
            )
        } else {
            MioTopBarContent(
                state = state,
                onNavigationClick = onNavigationClick,
                onActionClick = onActionClick
            )
        }
    }
}

@Composable
private fun MioTopBarContent(
    state: MioTopBarState,
    onNavigationClick: () -> Unit,
    onActionClick: (MioTopBarActionType) -> Unit
) {
    val isCompact = state.density == MioTopBarDensity.Compact
    val minHeight = if (isCompact) MioSize.topBarCompactMinHeight else MioSize.topBarMinHeight
    val horizontalPadding = if (isCompact) MioSpacing.lg else MioSpacing.xxl
    val verticalPadding = if (isCompact) MioSpacing.sm else MioSpacing.lg
    val titleStyle = if (isCompact) {
        MaterialTheme.typography.titleLarge
    } else {
        MaterialTheme.typography.headlineMedium
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .heightIn(min = minHeight)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.align(alignment = Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.navigationIcon is MioTopBarNavigation.Back) {
                    MioTopBarActionButton(
                        iconRes = R.drawable.ic_arrow_back,
                        contentDescription = "返回",
                        style = MioTopBarActionStyle.Plain,
                        onClick = onNavigationClick
                    )
                }

                AnimatedContent(
                    targetState = state.title,
                    transitionSpec = {
                        (fadeIn(animationSpec = MioMotion.standardTween()) +
                            slideInVertically(
                                animationSpec = MioMotion.standardTween(),
                                initialOffsetY = { it / 4 }
                            ))
                            .togetherWith(
                                fadeOut(animationSpec = MioMotion.quickTween()) +
                                    slideOutVertically(
                                        animationSpec = MioMotion.quickTween(),
                                        targetOffsetY = { -it / 4 }
                                    )
                            )
                    },
                    label = "MioTopBarTitle"
                ) { title ->
                    Text(
                        text = title,
                        style = titleStyle,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            AnimatedContent(
                targetState = state.actions,
                modifier = Modifier.align(Alignment.CenterEnd),
                transitionSpec = {
                    (fadeIn(animationSpec = MioMotion.standardTween()) +
                        scaleIn(
                            animationSpec = MioMotion.standardTween(),
                            initialScale = 0.92f
                        ))
                        .togetherWith(
                            fadeOut(animationSpec = MioMotion.quickTween()) +
                                scaleOut(
                                    animationSpec = MioMotion.quickTween(),
                                    targetScale = 0.92f
                                )
                        )
                        .using(SizeTransform(clip = false))
                },
                label = "MioTopBarActions"
            ) { actions ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    actions.forEach { action ->
                        MioTopBarActionButton(
                            iconRes = action.iconRes,
                            contentDescription = action.contentDescription,
                            style = action.style,
                            onClick = { onActionClick(action.type) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MioTopBarSearchField(
    density: MioTopBarDensity,
    query: String,
    onQueryChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val isCompact = density == MioTopBarDensity.Compact
    val minHeight = if (isCompact) MioSize.topBarCompactMinHeight else MioSize.topBarMinHeight
    val horizontalPadding = if (isCompact) MioSpacing.lg else MioSpacing.xxl
    val verticalPadding = if (isCompact) MioSpacing.sm else MioSpacing.lg

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .heightIn(min = minHeight)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MioSize.actionPillHeight),
                shape = RoundedCornerShape(MioRadius.pill),
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = MioSpacing.lg)
                ) {
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (query.isEmpty()) {
                                    Text(
                                        text = "搜索",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_topbar_close),
                            contentDescription = "关闭搜索",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MioTopBarActionButton(
    iconRes: Int,
    contentDescription: String,
    style: MioTopBarActionStyle,
    onClick: () -> Unit
) {
    val containerColor = when (style) {
        MioTopBarActionStyle.Plain -> Color.Transparent
        MioTopBarActionStyle.Filled -> MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val iconColor = when (style) {
        MioTopBarActionStyle.Plain -> MaterialTheme.colorScheme.onBackground
        MioTopBarActionStyle.Filled -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = Modifier
            .padding(start = MioSpacing.sm)
            .size(MioSize.iconContainer),
        onClick = onClick,
        shape = RoundedCornerShape(MioRadius.pill),
        color = containerColor
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                tint = iconColor
            )
        }
    }
}
