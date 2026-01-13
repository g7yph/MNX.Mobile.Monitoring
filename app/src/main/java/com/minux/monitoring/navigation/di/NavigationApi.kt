package com.minux.monitoring.navigation.di

import com.minux.monitoring.feature.auth.api.AuthFeatureMediator
import com.minux.monitoring.feature.cryptos.api.CryptosFeatureMediator
import com.minux.monitoring.feature.flightsheets.api.FlightSheetsFeatureMediator
import com.minux.monitoring.feature.profile.api.ProfileFeatureMediator
import com.minux.monitoring.feature.rigs.api.RigsFeatureMediator
import com.minux.monitoring.injector.BaseApi

interface NavigationApi : BaseApi {
    val authFeatureMediator: AuthFeatureMediator
    val profileFeatureMediator: ProfileFeatureMediator
    val rigsFeatureMediator: RigsFeatureMediator
    val cryptosFeatureMediator: CryptosFeatureMediator
    val flightSheetsFeatureMediator: FlightSheetsFeatureMediator
}