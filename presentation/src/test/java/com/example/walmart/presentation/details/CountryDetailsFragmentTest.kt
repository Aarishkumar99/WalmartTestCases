package com.example.walmart.presentation.details

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.walmart.domain.model.Country
import com.example.walmart.presentation.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Matcher
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestWatcher() {
    val testCoroutineDispatcher = TestCoroutineDispatcher()
    val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testCoroutineScope.cleanupTestCoroutines()
    }
}

@ExperimentalCoroutinesApi
class CountryDetailsFragmentTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private lateinit var navController: NavController

    @Before
    fun setup() {
        navController = mock(NavController::class.java)
    }

    @Test
    fun `renderState should display correct country details`() {
        // Arrange
        val scenario = launchFragmentInContainer<CountryDetailsFragment>()

        val state = CountryDetailsViewModel.State(
            loading = false,
            errorMessage = null,
            country = Country("USA", "United States", "North America", "Washington D.C.")
        )

        val viewModel = mock(CountryDetailsViewModel::class.java)
        val stateFlow = MutableStateFlow(state)

        `when`(viewModel.state).thenReturn(stateFlow)

        scenario.onFragment { fragment ->
            fragment.viewModel = viewModel
            navController.setGraph(R.navigation.main_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Act
        scenario.onFragment { fragment ->
            fragment.renderState(state)
        }

        // Assert
        onView(withId(R.id.swipe_refresh_layout)).check(matches(not(isRefreshing())))
        onView(withId(R.id.error_message)).check(matches(withText(nullValue(String::class.java))))
        onView(withId(R.id.name_with_region_view)).check(matches(withText("United States (North America)")))
        onView(withId(R.id.code_view)).check(matches(withText("USA")))
        onView(withId(R.id.capital_view)).check(matches(withText("Washington D.C.")))
    }

    @Test
    fun `onEffect with Effect OnBack should navigate back`() {
        // Arrange
        val scenario = launchFragmentInContainer<CountryDetailsFragment>()

        val viewModel = mock(CountryDetailsViewModel::class.java)

        scenario.onFragment { fragment ->
            fragment.viewModel = viewModel
            navController.setGraph(R.navigation.main_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Act
        scenario.onFragment { fragment ->
            fragment.onEffect(CountryDetailsViewModel.Effect.OnBack)
        }

        // Assert
        verify(navController).popBackStack()
    }

    fun isRefreshing(): Matcher<View> {
        return object : BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout::class.java) {

            override fun matchesSafely(item: SwipeRefreshLayout): Boolean {
                return item.isRefreshing
            }

            override fun describeTo(description: org.hamcrest.Description?) {
                description?.appendText("is refreshing")
            }
        }
    }

}
