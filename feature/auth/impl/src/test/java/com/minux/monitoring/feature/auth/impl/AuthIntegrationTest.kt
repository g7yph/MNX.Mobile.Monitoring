package com.minux.monitoring.feature.auth.impl

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.minux.monitoring.core.network.api.session.SessionManager
import com.minux.monitoring.core.network.api.session.TokensDto
import com.minux.monitoring.feature.auth.impl.data.datasource.AuthApiService
import com.minux.monitoring.feature.auth.impl.data.repository.AuthRepository
import com.minux.monitoring.feature.auth.impl.data.repository.AuthRepositoryImpl
import com.minux.monitoring.feature.auth.impl.presentation.ui.login.LoginAction
import com.minux.monitoring.feature.auth.impl.presentation.ui.login.LoginEvent
import com.minux.monitoring.feature.auth.impl.presentation.ui.login.LoginViewModel
import com.minux.monitoring.feature.auth.impl.retrofit.FlowResultCallAdapterFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.mock.Calls
import retrofit2.mock.MockRetrofit
import retrofit2.mock.create

class AuthIntegrationTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://example.com")
            .addCallAdapterFactory(FlowResultCallAdapterFactory.create())
            .build()

        val mockApi = MockRetrofit.Builder(retrofit)
            .build()
            .create<AuthApiService>()

        sessionManager = mockk<SessionManager>()

        authRepository = AuthRepositoryImpl(
            authApiService = mockApi.returning(
                Calls.response(TokensDto("", "", ""))
            ),
            sessionManager = sessionManager
        )
    }

    @Test
    fun `login flow integrates ViewModel, Repository and SessionManager`() = runTest {
        val viewModel = LoginViewModel(authRepository = authRepository)

        coEvery { sessionManager.updateCredentials(any()) } returns Unit

        viewModel.onEvent(LoginEvent.LoginChanged("test@example.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("ValidPass123!"))
        viewModel.onEvent(LoginEvent.Login)

        viewModel.uiActions().test {
            val action = awaitItem()
            assertThat(action).isInstanceOf(LoginAction.OpenMainScreen::class.java)
        }

        // Проверка сохранения токенов
        coVerify { sessionManager.updateCredentials(any()) }
    }
}