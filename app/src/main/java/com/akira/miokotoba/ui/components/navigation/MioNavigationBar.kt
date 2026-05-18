package com.akira.miokotoba.ui.components.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.akira.miokotoba.ui.navigation.BottomNavItem

@Composable
fun MioNavigationBar(
    selectedItem: BottomNavItem,
    onScreenSelected: (BottomNavItem) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 8.dp
    ) {
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = { onScreenSelected(item) },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label
                    )
                }
            )
        }
    }
}