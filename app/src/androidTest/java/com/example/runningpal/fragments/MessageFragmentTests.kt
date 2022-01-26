package com.example.runningpal.fragments

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.runningpal.R
import org.junit.Rule
import org.junit.Test

class MessageFragmentTests {

    @Test
    fun isFragmentInView() {
        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<MessageFragment>(fragmentArgs)
        Espresso.onView(ViewMatchers.withId(R.id.messageFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }


    @Test
    fun testVisibilityFABIsVisible() {

        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<MessageFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.fabMessageFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityRecyclerViewIsVisible() {

        val fragmentArgs = bundleOf("0" to 0)
        launchFragmentInContainer<MessageFragment>(fragmentArgs)

        Espresso.onView(ViewMatchers.withId(R.id.rvMessageFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }


}

