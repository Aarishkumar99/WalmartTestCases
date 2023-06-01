import com.example.walmart.data.mapper.CountryMapper
import com.example.walmart.data.network.response.CountryResponse
import com.example.walmart.domain.model.Country
import org.junit.Assert.assertEquals
import org.junit.Test

class CountryMapperTest {
    @Test
    fun `test toModel`() {
        val mapper = CountryMapper()

        val response = CountryResponse(
            name = "United States",
            region = "North America",
            code = "US",
            capital = "Washington, D.C."
        )

        val expectedCountry = Country(
            name = "United States",
            region = "North America",
            code = "US",
            capital = "Washington, D.C."
        )

        val actualCountry = mapper.toModel(response)

        assertEquals(expectedCountry, actualCountry)
    }
}
