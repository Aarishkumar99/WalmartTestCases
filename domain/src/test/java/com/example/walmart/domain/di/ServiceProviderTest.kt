import com.example.walmart.domain.di.ServiceModule
import com.example.walmart.domain.di.ServiceProvider
import kotlin.reflect.KClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ServiceProviderTest {
    interface SomeDependency

    class SomeDependencyImpl : SomeDependency

    class TestModule : ServiceModule {
        private val dependencies = mutableMapOf<KClass<*>, () -> Any>()

        override fun <T : Any> add(kClass: KClass<T>, creation: () -> T) {
            dependencies[kClass] = creation
        }

        override fun dependencies(): Map<KClass<*>, () -> Any> = dependencies

        override fun <T> get(kClass: KClass<*>): T = ServiceProvider.get(kClass)
    }

    @Before
    fun setup() {
        val module = TestModule()
        module.add(SomeDependency::class) { SomeDependencyImpl() }
        ServiceProvider.initialize(module)
    }

    @Test
    fun `test get method`() {
        val dependency: SomeDependency = ServiceProvider.get()

        assertEquals(SomeDependencyImpl::class, dependency.javaClass)
    }
}
