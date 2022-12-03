package com.udacity.project4.locationreminders

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@LargeTest
@RunWith(AndroidJUnit4::class)
class RemindersActivityTest {

    private lateinit var scenario: ActivityScenario<RemindersActivity>

    @Before
    fun setup() {
        stopKoin()
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.INITIALIZED)
    }

    @Test
    fun testAnyThing() {
        onView(withId(com.udacity.project4.R.id.textView_id))
    }


}