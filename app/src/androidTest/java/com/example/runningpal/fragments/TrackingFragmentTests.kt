package com.example.runningpal.fragments

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import com.example.runningpal.R
import org.junit.Test

class TrackingFragmentTests {

    @Test
    fun isFragmentInView() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<TrackingFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.mapsFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityMapViewIsVisible() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<TrackingFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.mapView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityRecyclerViewIsVisible() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<TrackingFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.rvRuns)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilitySpinnerIsVisible() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<TrackingFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.spFilter)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityButtonIsVisible() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<TrackingFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.btnNewRun)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityButtonIsInvisible() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<TrackingFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.btnFinishRun)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))}

}