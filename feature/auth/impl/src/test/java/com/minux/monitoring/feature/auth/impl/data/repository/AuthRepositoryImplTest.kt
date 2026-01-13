package com.minux.monitoring.feature.auth.impl.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.minux.monitoring.core.network.api.session.SessionManager
import com.minux.monitoring.core.network.api.session.TokensDto
import com.minux.monitoring.feature.auth.impl.data.datasource.AuthApiService
import com.minux.monitoring.feature.auth.impl.data.model.AuthInfoDto
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class AuthRepositoryImplTest {

    private lateinit var authApiService: AuthApiService
    private lateinit var sessionManager: SessionManager
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        authApiService = mockk<AuthApiService>()
        sessionManager = mockk<SessionManager>()

        repository = AuthRepositoryImpl(
            authApiService = authApiService,
            sessionManager = sessionManager
        )
    }

    @Test
    fun `authUser returns success flow when API call succeeds`() = runTest {
        val authInfo = AuthInfoDto(login = "test@example.com", password = "password")
        val tokens = TokensDto(accessToken = "access", refreshToken = "refresh")

        every { authApiService.authUser(authInfo) } returns flowOf(Result.success(tokens))
        coEvery { sessionManager.updateCredentials(tokens) } just Runs

        repository.authUser(authInfo).test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            awaitComplete()
        }

        coVerify { sessionManager.updateCredentials(tokens) }
    }

    @Test
    fun `authUser returns failure flow when API call fails`() = runTest {
        val authInfo = AuthInfoDto(login = "test@example.com", password = "password")
        val exception = IOException("Network error")

        every { authApiService.authUser(authInfo) } returns flowOf(Result.failure(exception))

        repository.authUser(authInfo).test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }

        coVerify(exactly = 0) { sessionManager.updateCredentials(any()) }
    }
}