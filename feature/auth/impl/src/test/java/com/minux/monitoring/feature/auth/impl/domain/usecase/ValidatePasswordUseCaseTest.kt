package com.minux.monitoring.feature.auth.impl.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.minux.monitoring.feature.auth.api.model.PasswordValidationError
import com.minux.monitoring.feature.auth.api.model.PasswordValidationResult
import org.junit.Before
import org.junit.Test

class ValidatePasswordUseCaseTest {
    private lateinit var useCase: ValidatePasswordUseCase

    @Before
    fun setUp() {
        useCase = ValidatePasswordUseCase()
    }

    @Test
    fun `validate password with valid input returns Valid`() {
        val password = "ValidPass123!"
        val result = useCase(password)

        assertThat(result).isInstanceOf(PasswordValidationResult.Valid::class.java)
    }

    @Test
    fun `validate password with short length returns Invalid`() {
        val password = "Short1!"
        val result = useCase(password)

        assertThat(result).isInstanceOf(PasswordValidationResult.Invalid::class.java)
    }

    @Test
    fun `validate password without uppercase returns Invalid`() {
        val password = "validpass123!"
        val result = useCase(password)

        assertThat(result).isInstanceOf(PasswordValidationResult.Invalid::class.java)
        val invalidResult = result as PasswordValidationResult.Invalid
        assertThat(invalidResult.errors).contains(PasswordValidationError.MissingUppercase)
    }

    @Test
    fun `validate password without digit returns Invalid`() {
        val password = "ValidPass!"
        val result = useCase(password)

        assertThat(result).isInstanceOf(PasswordValidationResult.Invalid::class.java)
        val invalidResult = result as PasswordValidationResult.Invalid
        assertThat(invalidResult.errors).contains(PasswordValidationError.MissingDigit)
    }
}