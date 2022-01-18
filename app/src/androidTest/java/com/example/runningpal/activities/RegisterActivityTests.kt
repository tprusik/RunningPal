package com.example.runningpal.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.runningpal.R
import org.junit.Test

class RegisterActivityTests {

    // podstawowe testy UI
    @Test
    fun isActivityInView() {

        ActivityScenario.launch(RegisterActivity::class.java)
        onView(ViewMatchers.withId(R.id.register_activity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun testVisibilityButtonIsVisible() {

       ActivityScenario.launch(RegisterActivity::class.java)

        onView(ViewMatchers.withId(R.id.btnApplyRegister)).check(matches(isDisplayed()))

    }

    @Test
    fun testVisibilityEditTextIsVisible() {

       ActivityScenario.launch(RegisterActivity::class.java)

        onView(ViewMatchers.withId(R.id.etRegisterEmail)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.etRegisterNick)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.etRegisterPass)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

    }

    @Test
    fun testVisibilityTitleIsDisplayed() {

        ActivityScenario.launch(RegisterActivity::class.java)

        onView(ViewMatchers.withId(R.id.tvRegister)).check(matches(withText(R.string.register_title)))

    }




}