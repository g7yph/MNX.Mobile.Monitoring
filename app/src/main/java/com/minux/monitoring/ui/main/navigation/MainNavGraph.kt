package com.minux.monitoring.ui.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.minux.monitoring.navigation.di.NavigationApi

@Composable
internal fun MainNavGraph(
    navController: NavHostController,
    navigationApi: NavigationApi,
    onShowSnackBar: (message: String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MainFlowRoute.Rigs
    ) {
        composable<MainFlowRoute.Rigs> { entry ->
            navigationApi.rigsFeatureMediator
                .AddRigsScreen(entry = entry, onShowSnackBar = onShowSnackBar)
        }

        composable<MainFlowRoute.Mining.Cryptos> { entry ->
            navigationApi.cryptosFeatureMediator
                .AddCryptosScreen(entry = entry, onShowSnackBar = onShowSnackBar)
        }

        composable<MainFlowRoute.Mining.Wallets> { entry ->
            navigationApi.cryptosFeatureMediator
                .AddWalletsScreen(entry = entry, onShowSnackBar = onShowSnackBar)
        }

        composable<MainFlowRoute.Mining.Pools> { entry ->
            navigationApi.cryptosFeatureMediator
                .AddPoolsScreen(entry = entry, onShowSnackBar = onShowSnackBar)
        }

        composable<MainFlowRoute.FlightSheets> { entry ->
            navigationApi.flightSheetsFeatureMediator
                .AddFlightSheetsFlowScreen(entry = entry, onShowSnackBar = onShowSnackBar)
        }
    }
}