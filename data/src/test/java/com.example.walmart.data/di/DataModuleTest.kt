import com.example.walmart.data.error.ErrorFormatterImpl
import com.example.walmart.data.mapper.CountryMapper
import com.example.walmart.data.repo.CountryRepoImpl
import com.example.walmart.domain.di.ServiceModule
import com.example.walmart.domain.di.add
import com.example.walmart.domain.di.get
import com.example.walmart.domain.di.module
import com.example.walmart.domain.error.ErrorFormatter
import com.example.walmart.domain.provider.DispatcherProvider
import com.example.walmart.domain.repo.CountryRepo
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class DataModuleTest {
    @Test
    fun `test module dependencies`() {
        val mockDispatcherProvider: DispatcherProvider = mockk()
        val mockCountryMapper: CountryMapper = mockk()

        val module: ServiceModule = module {
            add<DispatcherProvider> { mockDispatcherProvider }
            add<ErrorFormatter> { ErrorFormatterImpl(context = get()) }
            add<CountryRepo> { CountryRepoImpl(restService = get(), countryMapper = mockCountryMapper) }
            add { mockCountryMapper }
        }

        val dependencies = module.dependencies()

        assertEquals(4, dependencies.size)
        assertEquals(mockDispatcherProvider, dependencies[DispatcherProvider::class]?.invoke())
        assertEquals(ErrorFormatterImpl::class, dependencies[ErrorFormatter::class]?.invoke()?.javaClass)
        assertEquals(CountryRepoImpl::class, dependencies[CountryRepo::class]?.invoke()?.javaClass)
        assertEquals(mockCountryMapper, dependencies[CountryMapper::class]?.invoke())
    }
}
