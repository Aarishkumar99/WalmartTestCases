package com.example.walmart.domain.provider

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Test

class DispatcherProviderTest {

    @Test
    fun `io should return IO dispatcher`() {
        // Arrange
        val dispatcherProvider: DispatcherProvider = object : DispatcherProvider {}

        // Act
        val ioDispatcher = dispatcherProvider.io()

        // Assert
        assertEquals(Dispatchers.IO, ioDispatcher)
    }

    @Test
    fun `main should return Main dispatcher`() {
        // Arrange
        val dispatcherProvider: DispatcherProvider = object : DispatcherProvider {}

        // Act
        val mainDispatcher = dispatcherProvider.main()

        // Assert
        assertEquals(Dispatchers.Main.immediate, mainDispatcher)
    }

    @Test
    fun `default should return Default dispatcher`() {
        // Arrange
        val dispatcherProvider: DispatcherProvider = object : DispatcherProvider {}

        // Act
        val defaultDispatcher = dispatcherProvider.default()

        // Assert
        assertEquals(Dispatchers.Default, defaultDispatcher)
    }
}
