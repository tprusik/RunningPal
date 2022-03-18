package com.example.runningpal.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.runningpal.R
import org.junit.Test

class DashboardActivityTests {

    @Test
    fun isActivityInView() {

        ActivityScenario.launch(DashboardActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.drawerLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun testVisibilityBottomNavigationIsVisible() {

        ActivityScenario.launch(DashboardActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.bottomNavigationView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityBottomNavigationsIsVisible() {

        ActivityScenario.launch(DashboardActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.userProfileFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun navTestBottomNavigationViewToUserFragment() {

        ActivityScenario.launch(DashboardActivity::class.java)


        Espresso.onView(ViewMatchers.withId(R.id.userProfileFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.userProfileFragment)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.userProfileFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.drawerLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun navTestBottomNavigationViewToMessageFragment() {

        ActivityScenario.launch(DashboardActivity::class.java)


        Espresso.onView(ViewMatchers.withId(R.id.messageFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.messageFragment)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.messageFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.drawerLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun navTestBottomNavigationViewToRunFragment() {
        ActivityScenario.launch(DashboardActivity::class.java)


        Espresso.onView(ViewMatchers.withId(R.id.statisticsFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.statisticsFragment)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.runFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.drawerLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun navTestBottomNavigationViewToRunRoomFragment() {
        ActivityScenario.launch(DashboardActivity::class.java)


        Espresso.onView(ViewMatchers.withId(R.id.runRoomFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.runRoomFragment)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.runRoomFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.drawerLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @Test
    fun navTestBottomNavigationViewToTrackingRoomFragment() {
        ActivityScenario.launch(DashboardActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.statisticsFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.statisticsFragment)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.runFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.btnNewRun)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.mapsFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun navTestBottomNavigationViewToFindUserFragment() {
        ActivityScenario.launch(DashboardActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.userProfileFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.userProfileFragment)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.userProfileFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.btnUserProfileFindUser)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.findUserFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityCreateNewRoom() {
        ActivityScenario.launch(DashboardActivity::class.java)

        //VERIFY
        Espresso.onView(ViewMatchers.withId(R.id.runRoomFragment)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.runRoomFragment)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.runRoomFragmentLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //NEWFRAG
        Espresso.onView(ViewMatchers.withId(R.id.room_menu_new_room)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.room_menu_new_room)).perform(click())
        Espresso.onView(ViewMatchers.withId(R.id.rvRunRoom)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }
}

