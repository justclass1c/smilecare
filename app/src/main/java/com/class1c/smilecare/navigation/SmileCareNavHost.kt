package com.class1c.smilecare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.class1c.smilecare.features.appointments.presentation.AppointmentsRoute
import com.class1c.smilecare.features.dentists.presentation.DentistsRoute
import com.class1c.smilecare.features.home.presentation.HomeRoute

/** Single place where features are glued to routes. */
@Composable
fun SmileCareNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.path,
        modifier = modifier,
    ) {
        composable(Route.Home.path) {
            HomeRoute(
                onSeeAllClick = { navController.navigate(Route.Appointments.path) },
                onDentistsClick = { navController.navigate(Route.Dentists.path) },
            )
        }

        composable(Route.Appointments.path) {
            AppointmentsRoute(
                onBack = { navController.popBackStack() },
            )
        }

        composable(Route.Dentists.path) {
            DentistsRoute(
                viewModel = hiltViewModel(),
                modifier = Modifier,
            )
        }
    }
}
