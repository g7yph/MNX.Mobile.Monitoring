package com.minux.monitoring.ui.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.minux.monitoring.core.designsystem.component.MNXTopAppBar
import com.minux.monitoring.core.designsystem.icon.MNXIcons
import com.minux.monitoring.navigation.di.NavigationApi
import com.minux.monitoring.ui.main.component.MainNavigationDrawer
import com.minux.monitoring.ui.main.component.NavigationDrawerItemModel
import com.minux.monitoring.ui.main.navigation.MainFlowRoute
import com.minux.monitoring.ui.main.navigation.MainNavGraph
import kotlinx.coroutines.launch

@Composable
internal fun MainRoute(
    viewModel: MainViewModel,
    onNavigateToProfileSettingsScreen: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    navigationApi: NavigationApi,
    navController: NavHostController
) {
    val state by viewModel.uiStates().collectAsStateWithLifecycle()
    val action by viewModel.uiActions().collectAsStateWithLifecycle(initialValue = null)

    MainScreen(
        mainUiState = state,
        onEvent = viewModel::onEvent,
        onShowSnackBar = onShowSnackBar,
        navigationApi = navigationApi,
        navController = navController
    )

    when (action) {
        MainAction.OpenProfileSettingsScreen -> onNavigateToProfileSettingsScreen()

        null -> {}
    }

    if (action != null) viewModel.clearAction()
}

@Composable
private fun MainScreen(
    mainUiState: MainUiState,
    onEvent: (MainEvent) -> Unit,
    onShowSnackBar: (String) -> Unit,
    navigationApi: NavigationApi,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        onEvent(MainEvent.FetchProfileOverview)
    }

    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val items = listOf(
        NavigationDrawerItemModel.Single(
            route = MainFlowRoute.Rigs,
            title = "Rigs"
        ),
        NavigationDrawerItemModel.Group(
            items = listOf(
                NavigationDrawerItemModel.Single(
                    route = MainFlowRoute.Mining.Cryptos,
                    title = "Cryptocurrencies"
                ),
                NavigationDrawerItemModel.Single(
                    route = MainFlowRoute.Mining.Wallets,
                    title = "Wallets"
                ),
                NavigationDrawerItemModel.Single(
                    route = MainFlowRoute.Mining.Pools,
                    title = "Pools"
                )
            ),
            title = "Mining"
        ),
        NavigationDrawerItemModel.Single(
            route = MainFlowRoute.FlightSheets,
            title = "Flight Sheets"
        )
    )

    MainNavigationDrawer(
        drawerState = drawerState,
        profileOverviewModel = mainUiState.profileOverview,
        drawerItems = items,
        onProfileSettingsClick = { onEvent(MainEvent.ProfileSettings) },
        onNavigationDrawerItemClick = { route -> navController.navigate(route) },
        modifier = modifier
    ) {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            topBar = {
                MNXTopAppBar(
                    titleIconDrawableId = MNXIcons.Logo,
                    navigationIconDrawableId = MNXIcons.Menu,
                    onNavigationClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            contentWindowInsets = WindowInsets(0.dp)
        ) { scaffoldPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = scaffoldPadding)
            ) {
                MainNavGraph(
                    navController = navController,
                    navigationApi = navigationApi,
                    onShowSnackBar = onShowSnackBar
                )
            }
        }
    }
}