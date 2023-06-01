import com.example.walmart.data.network.CountryRestService
import com.example.walmart.data.network.EnvironmentProvider
import com.example.walmart.domain.di.ServiceModule
import com.example.walmart.domain.di.add
import com.example.walmart.domain.di.get
import com.example.walmart.domain.di.module
import com.google.gson.Gson
import io.mockk.every
import io.mockk.verify
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.mockk.mockk as mockk1

class NetworkModuleTest {
    @get:Rule
    val mockWebServerRule = MockWebServer()

    @Test
    fun `test network module dependencies`() {
        val mockEnvironmentProvider: EnvironmentProvider = mockk1()
        val mockRetrofitBuilder: Retrofit.Builder = mockk1()
        val mockRetrofit: Retrofit = mockk1()
        val mockCountryRestService: CountryRestService = mockk1()
        val mockGson: Gson = mockk1()
        val mockOkHttpClient: OkHttpClient = mockk1()

        val module: ServiceModule = module {
            add<EnvironmentProvider> { mockEnvironmentProvider }
            add<Retrofit.Builder> { mockRetrofitBuilder }
            add<Retrofit> { mockRetrofit }
            add<CountryRestService> { mockCountryRestService }
            add<Gson> { mockGson }
            add<OkHttpClient> { mockOkHttpClient }
        }

        val dependencies = module.dependencies()

        assertEquals(6, dependencies.size)
        assertEquals(mockEnvironmentProvider, dependencies[EnvironmentProvider::class]?.invoke())
        assertEquals(mockRetrofitBuilder, dependencies[Retrofit.Builder::class]?.invoke())
        assertEquals(mockRetrofit, dependencies[Retrofit::class]?.invoke())
        assertEquals(mockCountryRestService, dependencies[CountryRestService::class]?.invoke())
        assertEquals(mockGson, dependencies[Gson::class]?.invoke())
        assertEquals(mockOkHttpClient, dependencies[OkHttpClient::class]?.invoke())
    }

    @Test
    fun `test creating Retrofit instance`() {
        val mockEnvironmentProvider: EnvironmentProvider = mockk1()
        val mockRetrofitBuilder: Retrofit.Builder = mockk1()
        val mockRetrofit: Retrofit = mockk1()
        val mockGson: Gson = mockk1()
        val mockOkHttpClient: OkHttpClient = mockk1()

        every { mockRetrofitBuilder.addConverterFactory(any()) } returns mockRetrofitBuilder
        every { mockRetrofitBuilder.client(any()) } returns mockRetrofitBuilder
        every { mockRetrofitBuilder.build() } returns mockRetrofit

        val module: ServiceModule = module {
            add<EnvironmentProvider> { mockEnvironmentProvider }
            add<Retrofit.Builder> { mockRetrofitBuilder }
            add<Gson> { mockGson }
            add<OkHttpClient> { mockOkHttpClient }
        }

        val retrofitInstance = module.get<Retrofit>()

        assertEquals(mockRetrofit, retrofitInstance)
        verify {
            mockRetrofitBuilder.baseUrl(mockEnvironmentProvider.provideBaseUrl())
            mockRetrofitBuilder.addConverterFactory(GsonConverterFactory.create(mockGson))
            mockRetrofitBuilder.client(mockOkHttpClient)
            mockRetrofitBuilder.build()
        }
    }

    @Test
    fun `test creating CountryRestService instance`() {
        val mockRetrofit: Retrofit = mockk1()
        val mockCountryRestService: CountryRestService = mockk1()

        every { mockRetrofit.create(CountryRestService::class.java) } returns mockCountryRestService

        val module: ServiceModule = module {
            add<Retrofit> { mockRetrofit }
        }

        val countryRestServiceInstance = module.get<CountryRestService>()

        assertEquals(mockCountryRestService, countryRestServiceInstance)
        verify {
            mockRetrofit.create(CountryRestService::class.java)
        }
    }

}
