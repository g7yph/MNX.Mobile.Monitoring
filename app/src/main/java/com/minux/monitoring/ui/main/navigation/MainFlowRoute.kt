package com.minux.monitoring.ui.main.navigation

import kotlinx.serialization.Serializable

internal sealed interface MainFlowRoute {
    @Serializable
    data object Rigs : MainFlowRoute

    sealed interface Mining : MainFlowRoute {
        @Serializable
        data object Cryptos : Mining

        @Serializable
        data object Wallets : Mining

        @Serializable
        data object Pools : Mining
    }

    @Serializable
    data object FlightSheets : MainFlowRoute
}