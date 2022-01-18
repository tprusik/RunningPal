package com.example.runningpal.fragments
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.runningpal.R
import org.junit.Test

class UserProfileFragmentTests {

    @Test
    fun isFragmentInView() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

        Espresso.onView(withId(R.id.userProfileFragment)).check(matches(isDisplayed())) }

    @Test
    fun testVisibilityButtonIsVisible() {

        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

        Espresso.onView(withId(R.id.btnUserProfileFindUser)).check(matches(isDisplayed())) }

    @Test
    fun testVisibilityRecyclerViewIsVisible() {

        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

        Espresso.onView(withId(R.id.rvUserProvileFriends)).check(matches(isDisplayed())) }

    @Test
    fun testVisibilityImageViewIsVisible() {

        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

        Espresso.onView(withId(R.id.ivUserProfileAvatar)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.ivUserProfileBackground)).check(matches(isDisplayed())) }

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
        Espresso.onView(withId(R.id.tvUserProfileRunAmountInput)).check(matches(isDisplayed())) }

    @Test
    fun testVisibilityTextViewIsDisplayed() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<UserProfileFragment>(fragmentArgs)

        Espresso.onView(withId(R.id.tvUserProfileTotalDistance)).check(matches(withText(R.string.user_profile_total_distance)))
        Espresso.onView(withId(R.id.tvUserProfileTotalCaloriesBurned)).check(matches(withText(R.string.user_profile_total_calories)))
        Espresso.onView(withId(R.id.tvUserProfileTotalRunningTime)).check(matches(withText(R.string.user_profile_total_time)))
        Espresso.onView(withId(R.id.tvUserProfileRunAmount)).check(matches(withText(R.string.user_profile_total_runs))) }

}