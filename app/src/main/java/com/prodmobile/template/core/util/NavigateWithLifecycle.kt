package com.prodmobile.template.core.util

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

fun navigateWithLifecycle(
    navController: NavController,
    action: (NavController) -> Unit,
) {
    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        action(navController)
    }
}