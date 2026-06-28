package com.akira.miokotoba.ui.components.topbar

import androidx.annotation.DrawableRes

data class MioTopBarState(
    val title: String,
    val navigationIcon: MioTopBarNavigation = MioTopBarNavigation.None,
    val actions: List<MioTopBarAction> = emptyList(),
    val searchState: MioTopBarSearchState? = null
)

sealed interface MioTopBarNavigation {
    data object None : MioTopBarNavigation
    data object Back : MioTopBarNavigation
}

data class MioTopBarAction(
    @DrawableRes val iconRes: Int,
    val contentDescription: String,
    val type: MioTopBarActionType,
    val style: MioTopBarActionStyle = MioTopBarActionStyle.Plain
)

enum class MioTopBarActionType {
    Search,
    Settings,
    More
}

enum class MioTopBarActionStyle {
    Plain,
    Filled
}

data class MioTopBarSearchState(
    val query: String,
    val active: Boolean
)
