package com.minux.monitoring.feature.auth.impl.presentation.ui.login

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.minux.monitoring.feature.auth.impl.data.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class LoginViewModelTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        authRepository = mockk<AuthRepository>()

        viewModel = LoginViewModel(authRepository = authRepository)
    }

    @Test
    fun `onEvent LoginChanged updates email in state`() = runTest {
        val email = "test@example.com"

        viewModel.onEvent(LoginEvent.LoginChanged(email))

        viewModel.uiStates().test {
            val state = awaitItem()
            assertThat(state.authInfo.login).isEqualTo(email)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent Login with valid credentials navigates to main screen`() = runTest {
        val email = "test@example.com"
        val password = "ValidPass123!"

        every { authRepository.authUser(any()) } returns flowOf(Result.success(Unit))

        viewModel.onEvent(LoginEvent.LoginChanged(email))
        viewModel.onEvent(LoginEvent.PasswordChanged(password))
        viewModel.onEvent(LoginEvent.Login)

        viewModel.uiActions().test {
            val action = awaitItem()
            assertThat(action).isInstanceOf(LoginAction.OpenMainScreen::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent Login with invalid credentials shows error`() = runTest {
        val email = "test@example.com"
        val password = "invalid"
        val exception = IOException("Network error")

        every { authRepository.authUser(any()) } returns flowOf(Result.failure(exception))

        viewModel.onEvent(LoginEvent.LoginChanged(email))
        viewModel.onEvent(LoginEvent.PasswordChanged(password))
        viewModel.onEvent(LoginEvent.Login)

        viewModel.uiActions().test {
            val action = awaitItem()
            assertThat(action).isInstanceOf(LoginAction.ShowLoginFailedSnackBar::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}