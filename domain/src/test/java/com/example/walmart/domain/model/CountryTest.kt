package com.example.walmart.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CountryTest {

    @Test
    fun `country properties should have correct values`() {
        // Arrange
        val name = "Canada"
        val region = "North America"
        val code = "CA"
        val capital = "Ottawa"

        // Act
        val country = Country(name, region, code, capital)

        // Assert
        assertEquals(name, country.name)
        assertEquals(region, country.region)
        assertEquals(code, country.code)
        assertEquals(capital, country.capital)
    }
}
