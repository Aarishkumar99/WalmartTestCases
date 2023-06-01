package com.example.walmart.domain.usecase

import com.example.walmart.domain.model.Country
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchCountryUseCaseTest {

    @Test
    fun `searchCountryUseCase should return the same list when query is blank`() {
        // Arrange
        val useCase = SearchCountryUseCase()
        val countryList = listOf(
            Country("CA", "Canada", "North America", "Ottawa"),
            Country("BR", "Brazil", "South America", "Brasília"),
            Country("JP", "Japan", "Asia", "Tokyo"),
            Country("DE", "Germany", "Europe", "Berlin")
        )
        val query = ""

        // Act
        val result = useCase(countryList, query)

        // Assert
        assertEquals(countryList, result)
    }

    @Test
    fun `searchCountryUseCase should return filtered list based on query`() {
        // Arrange
        val useCase = SearchCountryUseCase()
        val countryList = listOf(
            Country("Canada", "North America", "CA", "Ottawa"),
            Country("Brazil", "South America", "BR", "Brasília"),
            Country("Japan", "Asia", "JP", "Tokyo"),
            Country("Germany", "Europe", "DE", "Berlin")
        )
        val query = "america"

        // Act
        val result = useCase(countryList, query)

        // Assert
        assertEquals(
            listOf(
                Country("Canada", "North America", "CA", "Ottawa"),
                Country("Brazil", "South America", "BR", "Brasília")
            ),
            result
        )
    }
}
