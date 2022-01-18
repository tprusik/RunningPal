package com.example.runningpal.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.runningpal.R
import org.junit.Test

class ActivityChatTests {

    // podstawowe testy UI
    @Test
    fun isActivityInView() {
        ActivityScenario.launch(ChatActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.chatActivity)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityButtonIsVisible() {
        ActivityScenario.launch(ChatActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.ibChatPostMessage)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())) }

    @Test
    fun testVisibilityEditTextIsVisible() {

        ActivityScenario.launch(ChatActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.etChatPostMessage)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))) }

    @Test
    fun testVisibilityTextViewIsVisible() {
        ActivityScenario.launch(ChatActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.tvChatActivity)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))) }

    @Test
    fun testVisibilityRecyclerViewIsVisible() {

        ActivityScenario.launch(ChatActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.rvChat)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))) }

    @Test
    fun testVisibilityImageViewIsVisible() {

        ActivityScenario.launch(ChatActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.ivChatActivity)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))) }

    @Test
    fun testVisibilityTitleIsDisplayed() {

        ActivityScenario.launch(RegisterActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.tvRegister)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.register_title))) }

}