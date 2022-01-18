package com.example.runningpal
import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.android21buttons.fragmenttestrule.FragmentTestRule
import com.example.runningpal.activities.DashboardActivity
import com.example.runningpal.activities.MainActivity
import com.example.runningpal.db.User
import com.example.runningpal.fragments.TrackingFragment
import com.example.runningpal.fragments.UserProfileFragment
import com.example.runningpal.others.Utils
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test



/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleInstrumentedTest {

    //Tips - withefected visibility
    private lateinit var context : Context

    @Before
    fun setup(){
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    //UtilsKlasa

    @Test
    fun shared_prefs_return_user_object() {

       assertTrue(Utils.getUserSharedPrevs(context) is User)
    }

    @Test
    fun shared_prefs_return_logged_user_should_have_proper_data() {
        val fragmentArgs = bundleOf("numElements" to 0)

        val scenario = launchFragmentInContainer<UserProfileFragment>(fragmentArgs)


       onView(withId(R.id.rvUserProvileFriends)).check(matches(isDisplayed()))

     //   onView(withId(R.id.rvUserProvileFriends)).perform(RecyclerViewActions)
    }

    @Test
    fun isActivityInView() {

        val scenario = ActivityScenario.launch(MainActivity::class.java)

         onView(withId(R.id.activity_main)).check(matches(isDisplayed()))

        //   onView(withId(R.id.rvUserProvileFriends)).perform(RecyclerViewActions)
    }

    @Test
    fun shared_prefs_return_logged_user_should_have_proper_datas() {
        val fragmentArgs = bundleOf("numElements" to 0)

        val scenario = launchFragmentInContainer<TrackingFragment>(fragmentArgs)

        //  val scenario = launchFragmentInContainer<UserProfileFragment>(fragmentArgs)
       // onView(withId(R.id.rv)).check(matches(isDisplayed()))

    }




}