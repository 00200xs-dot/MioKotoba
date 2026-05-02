package com.akira.miokotoba.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.ui.Navigation

@Composable
fun MioNavigationBar(
    selectedScreen: Int,
    onScreenSelected: (Int) -> Unit
) {
    NavigationBar(
        // 使用 surfaceContainer 保持与顶栏一致的容器感
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 8.dp
    ) {
        Navigation.entries.forEachIndexed { index, navigation ->
            NavigationBarItem(
                selected = selectedScreen == index,
                onClick = { onScreenSelected(index) },
                label = {
                    Text(
                        text = navigation.label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = navigation.iconRes),
                        contentDescription = navigation.label
                    )
                }
            )
        }
    }
}