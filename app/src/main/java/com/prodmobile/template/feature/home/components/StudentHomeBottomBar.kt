package com.prodmobile.template.feature.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
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
import com.prodmobile.template.navigation.StudentHomeDestinations

enum class StudentBottomBarDestination(
    val direction: StudentHomeDestinations,
    val icon: ImageVector,
    val label: String
) {
    Requests(StudentHomeDestinations.Favourite, Icons.Default.Favorite, "Избранное"),
    General(StudentHomeDestinations.Feed, Icons.Default.AutoAwesome, "Лента"),
    Account(StudentHomeDestinations.Profile, Icons.Default.Person, "Аккаунт"),
    MyRequests(StudentHomeDestinations.MyRequests, Icons.Default.Person, "Мои заявки"),
//    MentorProfile(StudentHomeDestinations.MentorProfile(), Icons.Default.Person, "Профиль ментора"),
}

@Composable
fun StudentHomeBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentHierarchy = navBackStackEntry?.destination?.hierarchy
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    BottomAppBar(modifier = modifier) {
        StudentBottomBarDestination.entries.forEach { destination ->

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


