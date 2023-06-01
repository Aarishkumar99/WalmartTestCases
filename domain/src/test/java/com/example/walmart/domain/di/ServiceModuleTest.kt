import com.example.walmart.domain.di.ServiceModule
import com.example.walmart.domain.di.ServiceProvider
import com.example.walmart.domain.di.add
import com.example.walmart.domain.di.get
import com.example.walmart.domain.di.module
import io.mockk.every
import io.mockk.mockk
import kotlin.reflect.KClass
import org.junit.Assert.assertEquals
import org.junit.Test

class ServiceModuleTest {
    interface TestDependency

    @Test
    fun `test adding and retrieving dependencies`() {
        val module: ServiceModule = module {
            add(TestDependency::class) { TestDependencyImpl() }
        }

        val dependency: TestDependency? = module.get()

        assertEquals(TestDependencyImpl::class.java, dependency?.javaClass)
    }

    @Test
    fun `test dependencies method`() {
        val module: ServiceModule = module {
            add(TestDependency::class) { TestDependencyImpl() }
        }

        val dependencies = module.dependencies()

        assertEquals(1, dependencies.size)
        assertEquals(TestDependencyImpl::class.java, dependencies.keys.first().java)
    }

    @Test
    fun `test get method`() {
        val mockDependency: TestDependency = mockk()
        val serviceProviderMock: ServiceProvider = mockk()

        every { serviceProviderMock.get<TestDependency>(any()) } returns mockDependency

        val module = object : ServiceModule {
            private val map = mutableMapOf<KClass<*>, () -> Any>()

            override fun <T : Any> add(kClass: KClass<T>, creation: () -> T) {
                map[kClass] = creation
            }

            override fun dependencies(): Map<KClass<*>, () -> Any> = map

            override fun <T> get(kClass: KClass<*>): T = serviceProviderMock.get(kClass)
        }.apply {
            add(TestDependency::class) { TestDependencyImpl() }
        }

        val dependency: TestDependency = module.get()

        assertEquals(mockDependency, dependency)
    }
}

class TestDependencyImpl : ServiceModuleTest.TestDependency
