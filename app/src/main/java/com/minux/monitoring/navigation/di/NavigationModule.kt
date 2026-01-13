package com.minux.monitoring.navigation.di

import com.minux.monitoring.feature.auth.api.AuthFeatureMediator
import com.minux.monitoring.feature.cryptos.api.CryptosFeatureMediator
import com.minux.monitoring.feature.flightsheets.api.FlightSheetsFeatureMediator
import com.minux.monitoring.feature.profile.api.ProfileFeatureMediator
import com.minux.monitoring.feature.rigs.api.RigsFeatureMediator
import com.minux.monitoring.navigation.AuthFeatureMediatorProxy
import com.minux.monitoring.navigation.CryptosFeatureMediatorProxy
import com.minux.monitoring.navigation.FlightSheetsFeatureMediatorProxy
import com.minux.monitoring.navigation.ProfileFeatureMediatorProxy
import com.minux.monitoring.navigation.RigsFeatureMediatorProxy
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal interface NavigationModule {

    @Binds
    @Singleton
    fun bindAuthFeatureMediator(
        authFeatureMediatorProxy: AuthFeatureMediatorProxy
    ): AuthFeatureMediator

    @Binds
    @Singleton
    fun bindProfileFeatureMediator(
        profileFeatureMediatorProxy: ProfileFeatureMediatorProxy
    ): ProfileFeatureMediator

    @Binds
    @Singleton
    fun bindRigsFeatureMediator(
        rigsFeatureMediatorProxy: RigsFeatureMediatorProxy
    ): RigsFeatureMediator

    @Binds
    @Singleton
    fun bindCryptosFeatureMediator(
        cryptosFeatureMediatorProxy: CryptosFeatureMediatorProxy
    ): CryptosFeatureMediator

    @Binds
    @Singleton
    fun bindFlightSheetsFeatureMediator(
        flightSheetsFeatureMediatorProxy: FlightSheetsFeatureMediatorProxy
    ): FlightSheetsFeatureMediator
}