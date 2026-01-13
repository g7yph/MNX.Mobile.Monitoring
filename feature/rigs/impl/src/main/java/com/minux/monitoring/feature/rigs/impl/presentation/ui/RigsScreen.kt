package com.minux.monitoring.feature.rigs.impl.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.minux.monitoring.core.designsystem.theme.MNXTheme
import com.minux.monitoring.core.designsystem.theme.MNXTypography
import com.minux.monitoring.core.ui.SearchTextField
import com.minux.monitoring.feature.rigs.impl.presentation.ui.component.Rigs
import com.minux.monitoring.feature.rigs.impl.presentation.ui.component.RigsError
import com.minux.monitoring.feature.rigs.impl.presentation.ui.component.RigsShimmer
import com.minux.monitoring.feature.rigs.impl.presentation.ui.component.RigsUiStatePreviewParameterProvider
import com.minux.monitoring.feature.rigs.impl.presentation.ui.model.RigsAction
import com.minux.monitoring.feature.rigs.impl.presentation.ui.model.RigsEvent
import com.minux.monitoring.feature.rigs.impl.presentation.ui.model.RigsUiState
import kotlinx.coroutines.delay

@Composable
internal fun RigsRoute(
    viewModel: RigsViewModel,
    onShowSnackBar: (String) -> Unit
) {
    val state by viewModel.uiStates().collectAsStateWithLifecycle()
    val action by viewModel.uiActions().collectAsStateWithLifecycle(initialValue = null)

    RigsScreen(
        rigsUiState = state,
        onEvent = viewModel::onEvent,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    )

    when (action) {
        is RigsAction.ShowPowerOffFailedSnackBar -> onShowSnackBar("Power off rig failed")

        is RigsAction.ShowRebootFailedSnackBar -> onShowSnackBar("Reboot rig failed")

        is RigsAction.ShowStartMiningFailedSnackBar -> onShowSnackBar("Start mining on rig failed")

        is RigsAction.ShowStopMiningFailedSnackBar -> onShowSnackBar("Stop mining on rig failed")
    }

    if (action != null) viewModel.clearAction()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RigsScreen(
    rigsUiState: RigsUiState,
    onEvent: (RigsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        onEvent(RigsEvent.FetchRigs)
    }

    Column(modifier = modifier) {
        Text(
            text = "Rigs",
            color = MaterialTheme.colorScheme.onBackground,
            style = MNXTypography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        SearchTextField(
            query = rigsUiState.searchQuery,
            onQueryChange = { onEvent(RigsEvent.SearchQueryChanged(searchQuery = it)) },
            enabled = !rigsUiState.rigsIsLoading && !rigsUiState.rigs.isNullOrEmpty(),
            placeholder = { Text(text = "Search") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        val isRefreshing = remember { mutableStateOf(false) }

        LaunchedEffect(isRefreshing.value) {
            if (isRefreshing.value) {
                onEvent(RigsEvent.FetchRigs)
                delay(200)
                isRefreshing.value = false
            }
        }

        PullToRefreshBox(
            isRefreshing = isRefreshing.value,
            onRefresh = { isRefreshing.value = true },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                when {
                    rigsUiState.rigsIsLoading -> RigsShimmer()

                    rigsUiState.filteredRigs == null -> RigsError(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )

                    else -> Rigs(
                        rigs = rigsUiState.filteredRigs,
                        onStartMiningClick = { onEvent(RigsEvent.StartMining(id = it)) },
                        onStopMiningClick = { onEvent(RigsEvent.StopMining(id = it)) },
                        onRebootClick = { onEvent(RigsEvent.Reboot(id = it)) },
                        onPowerOffClick = { onEvent(RigsEvent.PowerOff(id = it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RigsScreenPreview(
    @PreviewParameter(RigsUiStatePreviewParameterProvider::class)
    rigsUiState: RigsUiState
) {
    MNXTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RigsScreen(
                rigsUiState = rigsUiState,
                onEvent = {},
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}