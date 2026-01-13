package com.minux.monitoring.feature.auth.impl.domain.validator

import com.google.common.truth.Truth.assertThat
import com.minux.monitoring.feature.auth.api.PasswordValidator
import com.minux.monitoring.feature.auth.api.model.PasswordValidationResult
import com.minux.monitoring.feature.auth.impl.domain.usecase.ValidatePasswordConfirmUseCase
import com.minux.monitoring.feature.auth.impl.domain.usecase.ValidatePasswordUseCase
import org.junit.Before
import org.junit.Test

class PasswordValidatorImplTest {

    private lateinit var validator: PasswordValidator

    @Before
    fun setUp() {
        validator = PasswordValidatorImpl(
            validatePasswordUseCase = ValidatePasswordUseCase(),
            validatePasswordConfirmUseCase = ValidatePasswordConfirmUseCase()
        )
    }

    @Test
    fun `validate returns Valid for correct password`() {
        val result = validator.validate("ValidPass123!")

        assertThat(result).isInstanceOf(PasswordValidationResult.Valid::class.java)
    }

    @Test
    fun `validateConfirm returns true for matching passwords`() {
        val password = "ValidPass123!"
        val passwordConfirm = "ValidPass123!"

        val result = validator.validateConfirm(password, passwordConfirm)

        assertThat(result).isTrue()
    }

    @Test
    fun `validateConfirm returns false for non-matching passwords`() {
        val password = "ValidPass123!"
        val passwordConfirm = "DifferentPass123!"

        val result = validator.validateConfirm(password, passwordConfirm)

        assertThat(result).isFalse()
    }
}