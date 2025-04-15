package com.prodmobile.template.feature.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.prodmobile.template.core.util.navigateWithBottomBar
import com.prodmobile.template.navigation.MentorHomeDestinations

enum class MentorBottomBarDestination(
    val direction: Any,
    val icon: ImageVector,
    val label: String
) {
    General(MentorHomeDestinations.Requests, Icons.Default.AutoAwesome, "Заявки"),
    Account(MentorHomeDestinations.Profile, Icons.Default.Person, "Аккаунт"),
}

@Composable
fun MentorHomeBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentHierarchy = navBackStackEntry?.destination?.hierarchy
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    BottomAppBar(modifier = modifier) {
        MentorBottomBarDestination.entries.forEach { destination ->

            val isSelected by remember(currentRoute) {
                derivedStateOf {
                    currentHierarchy?.any { it.hasRoute(destination.direction::class) } ?: false
                }
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navigateWithBottomBar(
                        destination = destination.direction,
                        navController = navController
                    )
                },
                icon = { Icon(destination.icon, contentDescription = null) },
                label = { Text(destination.label) },
            )
        }
    }
}


