package com.example.runningpal.fragments

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.runningpal.R
import org.junit.Test

class RunFragmentTests {

    @Test
    fun isFragmentInView() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<StatisticsFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.runFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }


    @Test
    fun testVisibilityRecyclerViewIsVisible() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<StatisticsFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.rvRuns)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilitySpinnerIsVisible() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<StatisticsFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.spFilter)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityButtonIsVisible() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<StatisticsFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.btnNewRun)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }



}
