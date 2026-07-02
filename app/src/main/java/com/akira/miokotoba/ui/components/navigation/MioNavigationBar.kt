package com.akira.miokotoba.ui.components.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.ui.design.MioMotion
import com.akira.miokotoba.ui.design.MioRadius
import com.akira.miokotoba.ui.navigation.BottomNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MioNavigationBar(
    selectedItem: BottomNavItem,
    onScreenSelected: (BottomNavItem) -> Unit
) {
    val navigationRipple = RippleConfiguration(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        rippleAlpha = RippleAlpha(
            draggedAlpha = 0.08f,
            focusedAlpha = 0.08f,
            hoveredAlpha = 0.04f,
            pressedAlpha = 0.08f
        )
    )

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(
            topStart = MioRadius.xl,
            topEnd = MioRadius.xl
        )
    ) {
        CompositionLocalProvider(LocalRippleConfiguration provides navigationRipple) {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 0.dp
            ) {
                BottomNavItem.entries.forEach { item ->
                    val selected = selectedItem == item
                    val iconScale by animateFloatAsState(
                        targetValue = if (selected) 1.20f else 1f,
                        animationSpec = MioMotion.standardTween(),
                        label = "MioNavigationBarIconScale"
                    )

                    NavigationBarItem(
                        selected = selected,
                        onClick = { onScreenSelected(item) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = item.label,
                                modifier = Modifier.scale(iconScale)
                            )
                        }
                    )
                }
            }
        }
    }
}
