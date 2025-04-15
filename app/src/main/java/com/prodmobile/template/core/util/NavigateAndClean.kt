package com.prodmobile.template.core.util

import androidx.navigation.NavController
import com.prodmobile.template.navigation.NavigationPath

fun NavController.navigateAndClean(route: Any) {
    navigate(route = route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
    graph.setStartDestination(route)
}

fun NavController.navigateAndSaveState(route: NavigationPath) {
    navigate(route = route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}