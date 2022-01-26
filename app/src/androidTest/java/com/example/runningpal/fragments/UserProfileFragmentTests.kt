package com.example.runningpal.fragments
import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.runningpal.R
import com.example.runningpal.db.User
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.junit.Assert
import org.junit.Test


class UserProfileFragmentTests {


    @Test
    fun isFragmentInView() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

        Espresso.onView(withId(R.id.userProfileFragmentLayout)).check(matches(isDisplayed())) }

    @Test
    fun testVisibilityButtonIsVisible() {

        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

        Espresso.onView(withId(R.id.btnUserProfileFindUser)).check(matches(isDisplayed()))
    }

        @Test
        fun testVisibilityRecyclerViewIsVisible() {

            val fragmentArgs = bundleOf("0" to 0)
            launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

            Espresso.onView(withId(R.id.rvUserProvileFriends)).check(matches(isDisplayed()))
        }

        @Test
        fun testVisibilityImageViewIsVisible() {

            val fragmentArgs = bundleOf("0" to 0)
            launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

            Espresso.onView(withId(R.id.ivUserProfileAvatar)).check(matches(isDisplayed()))
            Espresso.onView(withId(R.id.ivUserProfileBackground)).check(matches(isDisplayed()))
        }

        @Test
        fun testVisibilityTextViewIsVisible() {

            val fragmentArgs = bundleOf("0" to 0)
            launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

            Espresso.onView(withId(R.id.tvUserProfileName)).check(matches(isDisplayed()))
            Espresso.onView(withId(R.id.tvUserProfileTotalCaloriesBurned)).check(matches(isDisplayed()))
            Espresso.onView(withId(R.id.tvUserProfileTotalCaloriesBurnedInput)).check(matches(isDisplayed()))
            Espresso.onView(withId(R.id.tvUserProfileTotalDistance)).check(matches(isDisplayed()))
            Espresso.onView(withId(R.id.tvUserProfileTotalDistanceInput)).check(matches(isDisplayed()))
            Espresso.onView(withId(R.id.tvUserProfileTotalRunningTime)).check(matches(isDisplayed()))
            Espresso.onView(withId(R.id.tvUserProfileTotalRunningTimeInput)).check(matches(isDisplayed()))
            Espresso.onView(withId(R.id.tvUserProfileRunAmount)).check(matches(isDisplayed()))
            Espresso.onView(withId(R.id.tvUserProfileRunAmountInput)).check(matches(isDisplayed()))
        }

        @Test
        fun testVisibilityTextViewIsDisplayed() {
            val fragmentArgs = bundleOf("0" to 0)
            launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

            Espresso.onView(withId(R.id.tvUserProfileTotalDistance)).check(matches(withText(R.string.user_profile_total_distance)))
            Espresso.onView(withId(R.id.tvUserProfileTotalCaloriesBurned)).check(matches(withText(R.string.user_profile_total_calories)))
            Espresso.onView(withId(R.id.tvUserProfileTotalRunningTime)).check(matches(withText(R.string.user_profile_total_time)))
            Espresso.onView(withId(R.id.tvUserProfileRunAmount)).check(matches(withText(R.string.user_profile_total_runs)))
        }

        @Test
        fun testNavFindUsers() {

            val navController = TestNavHostController(
                    ApplicationProvider.getApplicationContext()
            )

            val fragmentArgs = bundleOf("0" to 0)
            val scenario = launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

            scenario.onFragment { fragment ->
                Navigation.setViewNavController(fragment.requireView(), navController) }

            Espresso.onView(withId(R.id.tvUserProfileTotalCaloriesBurned)).check(matches(isDisplayed())) }

    @Test
    fun testSelectedItemInDetailRecyclerView() {

        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

        Espresso.onView(withId(R.id.rvUserProvileFriends)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.rvUserProvileFriends)).perform() }

    //IntegrationTests

    @Test
    fun userTotalRunskShouldNotBeBelowZero() {

        val fragmentArgs = bundleOf("numElements" to 0)
        val scenario = launchFragment<UserProfileFragment>(fragmentArgs)

        scenario.onFragment { fragment ->
         var text =  Integer.parseInt(fragment.tvUserProfileTotalRunningTimeInput.text.toString())
            Assert.assertTrue(text>=0)
        }
    }




}


