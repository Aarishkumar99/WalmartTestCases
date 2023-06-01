import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.walmart.presentation.R
import com.example.walmart.presentation.countries.CountriesFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountriesFragmentTest {

    @Test
    fun testFragmentViews() {
        // Launch the fragment in a test container
        launchFragmentInContainer<CountriesFragment>()

        // Verify that the swipe refresh layout is displayed
        Espresso.onView(ViewMatchers.withId(R.id.swipe_refresh_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Verify that the country recycler view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.country_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))


        // Verify that the search view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.action_search))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Verify that the error Snackbar is not displayed initially
        Espresso.onView(ViewMatchers.withText(R.string.retry))
            .check(ViewAssertions.doesNotExist())

        // Perform a click action and verify the error Snackbar is displayed
        Espresso.onView(ViewMatchers.withId(R.id.swipe_refresh_layout))
            .perform(ViewActions.swipeDown())
        Espresso.onView(ViewMatchers.withText(R.string.retry))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
