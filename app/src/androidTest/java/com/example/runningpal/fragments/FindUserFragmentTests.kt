package com.example.runningpal.fragments

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.runningpal.R
import org.junit.Test

class FindUserFragmentTests
{

    @Test
    fun isFragmentInView() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<FindUserFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.findUserFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }


    @Test
    fun testVisibilityEditTextIsVisible() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<FindUserFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.etFindUser)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityButtonIsVisible() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<FindUserFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.btnFindUser)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }


    @Test
    fun testVisibilityRecyclerViewIsVisibleAfterButtonClicked() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<FindUserFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.rvFindContact)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

}