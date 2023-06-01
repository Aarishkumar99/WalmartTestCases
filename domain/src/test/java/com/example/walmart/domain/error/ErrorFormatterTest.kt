package com.example.walmart.domain.error

import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorFormatterTest {

    @Test
    fun `getDisplayErrorMessage should return the expected error message`() {
        // Arrange
        val errorFormatter = object : ErrorFormatter {
            override fun getDisplayErrorMessage(throwable: Throwable): String {
                return "An error occurred: ${throwable.message}"
            }
        }
        val throwable = RuntimeException("Test Exception")
        val expectedErrorMessage = "An error occurred: Test Exception"

        // Act
        val errorMessage = errorFormatter.getDisplayErrorMessage(throwable)

        // Assert
        assertEquals(expectedErrorMessage, errorMessage)
    }
}
