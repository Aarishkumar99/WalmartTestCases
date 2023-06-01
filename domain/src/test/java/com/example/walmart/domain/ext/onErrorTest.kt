package com.example.walmart.domain.ext

import kotlinx.coroutines.CoroutineExceptionHandler
import org.junit.Assert.assertEquals
import org.junit.Test

class OnErrorTest {

    @Test
    fun `onError should invoke the block with the throwable`() {
        // Arrange
        var invoked = false
        val exception = RuntimeException("Test Exception")
        val block: (Throwable) -> Unit = { throwable ->
            invoked = true
            assertEquals(exception, throwable)
        }
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            block(throwable)
        }

        // Act
        val result = runCatching {
            onError(block).handleException(exception)
        }

        // Assert
        assertEquals(true, invoked)
        assertEquals(Unit, result.getOrNull())
    }

    private fun onError(block: (Throwable) -> Unit): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            block(throwable)
        }
    }

    private fun CoroutineExceptionHandler.handleException(throwable: Throwable) {
        handleException(this, throwable)
    }
}
