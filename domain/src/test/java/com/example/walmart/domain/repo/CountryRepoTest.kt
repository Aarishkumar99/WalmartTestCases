package com.example.walmart.domain.repo

import com.example.walmart.domain.model.Country
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CountryRepoTest {

    @Test
    fun `getCountries should return a list of countries`() = runBlocking {
        // Arrange
        val repo: CountryRepo = object : CountryRepo {
            override suspend fun getCountries(): List<Country> {
                // Simulating the retrieval of countries
                return listOf(
                    Country("Canada", "North America", "CA", "Ottawa"),
                    Country("Brazil", "South America", "BR", "Brasília"),
                    Country("Japan", "Asia", "JP", "Tokyo"),
                    Country("Germany", "Europe", "DE", "Berlin")
                )
            }

            override suspend fun getCountryDetailsByCode(code: String): Country? {
                // Not used in this test case
                return null
            }
        }

        // Act
        val countries = repo.getCountries()

        // Assert
        assertEquals(4, countries.size)
    }

    @Test
    fun `getCountryDetailsByCode should return the country details for a given code`() = runBlocking {
        // Arrange
        val repo: CountryRepo = object : CountryRepo {
            override suspend fun getCountries(): List<Country> {
                // Not used in this test case
                return emptyList()
            }

            override suspend fun getCountryDetailsByCode(code: String): Country? {
                // Simulating the retrieval of country details by code
                return when (code) {
                    "CA" -> Country("Canada", "North America", "CA", "Ottawa")
                    "BR" -> Country("Brazil", "South America", "BR", "Brasília")
                    else -> null
                }
            }
        }

        // Act
        val country1 = repo.getCountryDetailsByCode("CA")
        val country2 = repo.getCountryDetailsByCode("BR")
        val country3 = repo.getCountryDetailsByCode("JP")

        // Assert
        assertEquals("Canada", country1?.name)
        assertEquals("South America", country2?.region)
        assertEquals(null, country3)
    }
}
