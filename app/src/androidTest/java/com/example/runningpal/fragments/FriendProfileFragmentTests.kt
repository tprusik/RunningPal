package com.example.runningpal.fragments
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.runningpal.R
import org.junit.Test

class FriendProfileFragmentTests {


    @Test
    fun isFragmentInView() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<FriendProfileFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.friendProfileFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }


    @Test
    fun testVisibilityImageViewIsVisible() {

        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<FriendProfileFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.ivFriendProfileAvatar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.ivFriendProfileBackground)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityTextViewIsVisible() {

        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<FriendProfileFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileName)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileTotalCaloriesBurned)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileTotalCaloriesBurnedInput)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileTotalDistance)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileTotalDistanceInput)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileTotalRunningTime)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileTotalRunningTimeInput)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileRunAmount)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileRunAmountInput)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityTextViewIsDisplayed() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<FriendProfileFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileTotalDistance)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.user_profile_total_distance)))
        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileTotalCaloriesBurned)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.user_profile_total_calories)))
        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileTotalRunningTime)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.user_profile_total_time)))
        Espresso.onView(ViewMatchers.withId(R.id.tvFriendProfileRunAmount)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.user_profile_total_runs))) } }
