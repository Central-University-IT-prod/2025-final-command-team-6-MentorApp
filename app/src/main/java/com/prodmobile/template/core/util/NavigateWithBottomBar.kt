package com.prodmobile.template.core.util

import androidx.navigation.NavController

fun navigateWithBottomBar(
    isRestoreState: Boolean = true,
    destination: Any,
    navController: NavController
) {
    navController.navigate(destination) {
        popUpTo(navController.graph.startDestinationId) { saveState = true }
        launchSingleTop = true

        if (isRestoreState) {
            restoreState = true
        }
    }
}
