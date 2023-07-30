package com.hamdan.forzenbook.compose.core.composewidgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.core.NavBarItem
import com.hamdan.forzenbook.core.NavigationItem
import com.hamdan.forzenbook.ui.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForzenbookTopAppBar(
    modifier: Modifier = Modifier,
    showBackIcon: Boolean = true,
    additionalOnBack: (() -> Unit)? = null,
    titleSection: @Composable () -> Unit,
    actions: @Composable (() -> Unit)? = null,
) {
    val navigator = LocalNavController.current
    TopAppBar(
        title = {
            titleSection()
        },
        modifier = modifier
            .fillMaxWidth()
            .shadow(MaterialTheme.dimens.borderGrid.x2),
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = if (showBackIcon) {
            {
                IconButton(
                    onClick = {
                        additionalOnBack?.invoke()
                        navigator?.navigateUp()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_arrow),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        } else {
            {}
        },
        actions = {
            if (actions != null) actions()
        }
    )
}

@Composable
fun ForzenbookBottomNavigationBar(navIcons: List<NavigationItem>) {
    val navigator = LocalNavController.current
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiary,
    ) {
        navIcons.forEach {
            NavigationBarItem(
                selected = navigator?.currentDestination?.route == it.page,
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = stringResource(it.description),
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = it.label),
                        color = MaterialTheme.colorScheme.onTertiary,
                    )
                },
                alwaysShowLabel = true,
                onClick = {
                    when (it.navBarItem) {
                        NavBarItem.PROFILE -> {
                            navigator?.navigate(it.page + "/null") {
                                popUpTo(it.page)
                                launchSingleTop = true
                            }
                        }

                        else -> {
                            navigator?.navigate(it.page) {
                                popUpTo(it.page)
                                launchSingleTop = true
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        }
    }
}
