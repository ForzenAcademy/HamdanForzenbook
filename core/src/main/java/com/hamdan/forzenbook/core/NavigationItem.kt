package com.hamdan.forzenbook.core

import androidx.annotation.StringRes

data class NavigationItem(
    val navBarItem: NavBarItem,
    val page: String,
    val icon: Int,
    @StringRes val label: Int,
    @StringRes val description: Int,
)

enum class NavBarItem {
    FEED, SEARCH, PROFILE
}
