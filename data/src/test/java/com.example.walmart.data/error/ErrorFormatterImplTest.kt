import android.content.Context
import com.example.walmart.data.R
import com.example.walmart.data.error.ErrorFormatterImpl
import com.example.walmart.domain.error.ErrorFormatter
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.UnknownHostException

class ErrorFormatterImplTest {
    @Test
    fun `test getDisplayErrorMessage with UnknownHostException`() {
        val mockContext: Context = mockk()
        val errorFormatter: ErrorFormatter = ErrorFormatterImpl(mockContext)

        val throwable = UnknownHostException()

        every { mockContext.getString(R.string.error_internet_connection) } returns "No internet connection"

        val errorMessage = errorFormatter.getDisplayErrorMessage(throwable)

        assertEquals("No internet connection", errorMessage)
    }

    @Test
    fun `test getDisplayErrorMessage with blank message`() {
        val mockContext: Context = mockk()
        val errorFormatter: ErrorFormatter = ErrorFormatterImpl(mockContext)

        val throwable = Throwable()

        every { mockContext.getString(R.string.error_unknown) } returns "Unknown error"

        val errorMessage = errorFormatter.getDisplayErrorMessage(throwable)

        assertEquals("Unknown error", errorMessage)
    }

    @Test
    fun `test getDisplayErrorMessage with non-blank message`() {
        val mockContext: Context = mockk()
        val errorFormatter: ErrorFormatter = ErrorFormatterImpl(mockContext)

        val throwable = Throwable("Some error message")

        val errorMessage = errorFormatter.getDisplayErrorMessage(throwable)

        assertEquals("Some error message", errorMessage)
    }
}
