package com.alexteodorovici.loadbalancer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexteodorovici.loadbalancer.ui.main.MainActivity
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.matchers.JUnitMatchers.containsString
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testRegisterServer_andDisplay() {
        // Enter server details
        onView(withId(R.id.inputId)).perform(typeText("1"), closeSoftKeyboard())
        onView(withId(R.id.inputUrl)).perform(typeText("http://server1.com"), closeSoftKeyboard())
        onView(withId(R.id.buttonRegister)).perform(click())
        Thread.sleep(1000)  // Ideally use IdlingResource

        // Check if the TextView with the correct text is displayed inside serversContainer
        onView(allOf(
            isDescendantOfA(withId(R.id.serversContainer)),
            withText(containsString("1: http://server1.com"))
        )).check(matches(isDisplayed()))
    }

    @Test
    fun testSelectNextServer_displaysSelected() {
        // Enter server details and register
        onView(withId(R.id.inputId)).perform(typeText("1"), closeSoftKeyboard())
        onView(withId(R.id.inputUrl)).perform(typeText("http://server1.com"), closeSoftKeyboard())
        onView(withId(R.id.buttonRegister)).perform(click())
        Thread.sleep(1000)

        // Select next server
        onView(withId(R.id.buttonSelect)).perform(click())
        Thread.sleep(1000)

        // Check if selected server text is updated
        onView(withId(R.id.selectedServerText)).check(matches(withText(containsString("Selected Server:"))))
    }
}