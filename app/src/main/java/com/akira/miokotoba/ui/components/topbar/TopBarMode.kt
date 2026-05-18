package com.akira.miokotoba.ui.components.topbar

sealed class TopBarMode {
    data object Default : TopBarMode()
    data object Search : TopBarMode()
    data object Focus : TopBarMode()
}