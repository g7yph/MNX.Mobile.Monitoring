package com.minux.monitoring.feature.profile.impl.presentation.ui.component

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.minux.monitoring.feature.profile.impl.presentation.model.ProfileModel
import com.minux.monitoring.feature.profile.impl.presentation.ui.model.ProfileUiState

internal class ProfileUiStatePreviewParameterProvider : PreviewParameterProvider<ProfileUiState> {
    override val values: Sequence<ProfileUiState> = sequenceOf(
        ProfileUiState(),
        ProfileUiState(
            profileIsLoading = false,
            profile = ProfileModel(
                id = "",
                login = "sample",
                nickname = "example",
                registrationDate = "31.08.2023",
                email = "example@example.com",
                telegram = "@example",
                key = null,
                keyIsLoading = false,
                emailConfirmed = false,
                telegramConfirmed = false
            )
        ),
        ProfileUiState(profileIsLoading = false)
    )
}