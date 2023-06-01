import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.walmart.domain.error.ErrorFormatter
import com.example.walmart.domain.model.Country
import com.example.walmart.domain.provider.DispatcherProvider
import com.example.walmart.domain.repo.CountryRepo
import com.example.walmart.domain.usecase.SearchCountryUseCase
import com.example.walmart.presentation.countries.CountriesViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CountriesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CountriesViewModel
    private lateinit var mockRepo: CountryRepo
    private lateinit var mockDispatchers: DispatcherProvider
    private lateinit var mockErrorFormatter: ErrorFormatter

    @Before
    fun setup() {
        mockRepo = mockk()
        mockDispatchers = mockk()
        mockErrorFormatter = mockk()
        MockKAnnotations.init(this)
        viewModel = CountriesViewModel(mockRepo, mockDispatchers, mockErrorFormatter)
    }

    @Test
    fun `loadItems should update state with correct items`() = runBlockingTest {
        // Arrange
        val mockCountries = getMockCountries()
        val expectedItems = searchCountryUseCase(mockCountries, "")

        coEvery { mockRepo.getCountries() } returns mockCountries

        // Act
        viewModel.loadItems()

        // Assert
        val state = viewModel.state.value
        assertEquals(expectedItems, state.items)
    }

    @Test
    fun `onItemClick should send OpenDetails effect`() = runBlockingTest {
        // Arrange
        val mockCountry = Country("USA", "United States", "North America", "Washington D.C.")
        val effectChannel = Channel<CountriesViewModel.Effect>(Channel.UNLIMITED)

        viewModel.effectChannel = effectChannel

        // Act
        viewModel.onItemClick(mockCountry)

        // Assert
        val effect = effectChannel.receiveAsFlow().single()
        assertEquals(CountriesViewModel.Effect.OpenDetails(mockCountry.code), effect)
    }

    private fun getMockCountries(): List<Country> {
        return listOf(
            Country("USA", "United States", "North America", "Washington D.C."),
            Country("CAN", "Canada", "North America", "Ottawa"),
            Country("AUS", "Australia", "Oceania", "Canberra")
        )
    }

    private fun searchCountryUseCase(countries: List<Country>, query: String): List<Country> {
        return countries.filter { it.name.contains(query, ignoreCase = true) }
    }
}
