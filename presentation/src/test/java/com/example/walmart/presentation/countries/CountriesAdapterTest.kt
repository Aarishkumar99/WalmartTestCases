import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.walmart.domain.model.Country
import com.example.walmart.presentation.R
import com.example.walmart.presentation.countries.CountriesAdapter
import com.example.walmart.presentation.countries.CountryViewHolder
import com.example.walmart.presentation.databinding.CountryItemBinding
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountriesAdapterTest {

    @Test
    fun testAdapterItemBinding() {
        // Create a test country item
        val testCountry = Country("USA", "United States", "North America", "Washington, D.C.")

        // Create a fake parent ViewGroup for testing
        val parent = object : ViewGroup(InstrumentationRegistry.getInstrumentation().targetContext) {
            override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
        }

        // Create a test ViewHolder with the test country item and binding
        val binding = CountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = CountryViewHolder(binding)

        // Bind the test country item to the ViewHolder
        viewHolder.bind(testCountry)

        // Verify that the ViewHolder is correctly bound with the test country item
        Espresso.onView(ViewMatchers.withId(R.id.name_with_region_view))
            .check(ViewAssertions.matches(ViewMatchers.withText("United States, North America")))
        Espresso.onView(ViewMatchers.withId(R.id.code_view))
            .check(ViewAssertions.matches(ViewMatchers.withText("USA")))
        Espresso.onView(ViewMatchers.withId(R.id.capital_view))
            .check(ViewAssertions.matches(ViewMatchers.withText("Washington, D.C.")))
    }

    @Test
    fun testItemClick() {
        // Create a test country item
        val testCountry = Country("USA", "United States", "North America", "Washington, D.C.")

        // Create a fake parent ViewGroup for testing
        val parent = object : ViewGroup(InstrumentationRegistry.getInstrumentation().targetContext) {
            override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
        }

        // Create a test adapter with the test item click listener
        val onItemClick: (Country) -> Unit = { country ->
            // Add assertion for the expected country item
            assert(country == testCountry)
        }
        val adapter = CountriesAdapter(onItemClick)

        // Create a test ViewHolder with the test country item and binding
        val binding = CountryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = CountryViewHolder(binding)

        // Bind the test country item to the ViewHolder
        viewHolder.bind(testCountry)

        // Set the ViewHolder as the click listener for the root view
        viewHolder.binding.root.setOnClickListener {
            adapter.onItemClickListener(testCountry)
        }

        // Perform a click action on the ViewHolder's root view
        Espresso.onView(ViewMatchers.withId(viewHolder.binding.root.id))
            .perform(ViewActions.click())
    }
}
