package com.minux.monitoring.feature.auth.impl.presentation.ui.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.minux.monitoring.feature.auth.impl.presentation.model.AuthInfoModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun login_screen_shows_error_when_login_is_empty() {
        val viewModel = mockk<LoginViewModel>()
        val uiState = LoginUiState(
            authInfo = AuthInfoModel(
                login = "",
                isValidationShowed = true,
                isLoginValid = false
            )
        )

        every { viewModel.uiStates() } returns MutableStateFlow(uiState)
        every { viewModel.uiActions() } returns MutableSharedFlow()
        every { viewModel.onEvent(any()) } just Runs

        composeTestRule.setContent {
            LoginRoute(
                viewModel = viewModel,
                onNavigate = {},
                onNavigateToMainScreen = {},
                onShowSnackBar = {}
            )
        }

        composeTestRule.onNodeWithText("Enter login").assertIsDisplayed()
    }

    @Test
    fun login_screen_navigates_to_main_when_login_succeeds() = runTest {
        val viewModel = mockk<LoginViewModel>()
        every { viewModel.uiStates() } returns MutableStateFlow(LoginUiState())
        every { viewModel.uiActions() } returns MutableSharedFlow<LoginAction?>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        ).apply {
            tryEmit(LoginAction.OpenMainScreen)
        }
        every { viewModel.onEvent(any()) } just Runs
        every { viewModel.clearAction() } just Runs

        composeTestRule.setContent {
            LoginRoute(
                viewModel = viewModel,
                onNavigate = {},
                onNavigateToMainScreen = {},
                onShowSnackBar = {}
            )
        }

        composeTestRule.waitForIdle()

        viewModel.uiActions().test {
            val action = awaitItem()
            assertThat(action).isInstanceOf(LoginAction.OpenMainScreen::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}