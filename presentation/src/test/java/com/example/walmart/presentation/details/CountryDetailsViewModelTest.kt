import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.walmart.domain.error.ErrorFormatter
import com.example.walmart.domain.model.Country
import com.example.walmart.domain.provider.DispatcherProvider
import com.example.walmart.domain.repo.CountryRepo
import com.example.walmart.presentation.details.CountryDetailsArg
import com.example.walmart.presentation.details.CountryDetailsViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CountryDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CountryDetailsViewModel
    private lateinit var repo: CountryRepo
    private lateinit var errorFormatter: ErrorFormatter
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setup() {
        repo = mockk()
        errorFormatter = mockk()
        savedStateHandle = mockk(relaxed = true)
        dispatcherProvider = mockk(relaxed = true)
        testDispatcher = TestCoroutineDispatcher()

        every { dispatcherProvider.io() } returns testDispatcher

        viewModel = CountryDetailsViewModel(savedStateHandle, repo, dispatcherProvider, errorFormatter)
    }

    @After
    fun teardown() {
        unmockkAll()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `loadCountry should update state with country details`() = runBlockingTest {
        val countryCode = "US"
        val mockCountry = Country("USA", "United States", "North America", "Washington, D.C.")
        val stateFlow = MutableStateFlow(CountryDetailsViewModel.State())

        every { savedStateHandle.get<String>(CountryDetailsArg.COUNTRY_CODE) } returns countryCode
        coEvery { repo.getCountryDetailsByCode(code = countryCode) } returns mockCountry

        val stateCapture = slot<CountryDetailsViewModel.State>()
        every { stateFlow.value = capture(stateCapture) } just runs
        every { viewModel.state } returns stateFlow

        viewModel.loadCountry()

        verify {
            stateFlow.value = stateCapture.captured.copy(loading = true)
            stateFlow.value = stateCapture.captured.copy(loading = false, country = mockCountry)
        }
        coVerify {
            repo.getCountryDetailsByCode(code = countryCode)
        }
    }

    // Add more test cases as needed
}
