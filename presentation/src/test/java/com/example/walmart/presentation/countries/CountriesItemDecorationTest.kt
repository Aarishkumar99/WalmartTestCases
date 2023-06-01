import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.walmart.presentation.countries.CountriesItemDecoration
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountriesItemDecorationTest {

    @Test
    fun testItemOffsets() {
        // Create a mock RecyclerView, view, and outRect
        val recyclerView = RecyclerView(InstrumentationRegistry.getInstrumentation().targetContext)
        val view = View(InstrumentationRegistry.getInstrumentation().targetContext)
        val outRect = Rect()

        // Set the adapter position and item count to test different scenarios
        val itemPosition = 2
        val itemCount = 5

        // Create a test instance of CountriesItemDecoration
        val itemDecoration = CountriesItemDecoration(10)

        // Create a mock RecyclerView.State with overridden getItemCount() function
        val state = object : RecyclerView.State() {
            override fun getItemCount(): Int {
                return itemCount
            }
        }

        // Set the test parameters in the itemDecoration.getItemOffsets function
        itemDecoration.getItemOffsets(outRect, view, recyclerView, state)

        // Verify the expected values of outRect based on the test parameters
        if (itemPosition == itemCount - 1) {
            // Last item should have bottom spacing
            assert(outRect.bottom == 10)
        } else {
            // Other items should not have bottom spacing
            assert(outRect.bottom == 0)
        }

        // Verify the expected values of outRect for left, top, and right spacing
        assert(outRect.left == 10)
        assert(outRect.top == 10)
        assert(outRect.right == 10)
    }
}
