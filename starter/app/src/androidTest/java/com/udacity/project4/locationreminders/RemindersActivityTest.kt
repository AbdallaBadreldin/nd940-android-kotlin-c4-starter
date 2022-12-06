package com.udacity.project4.locationreminders

import android.app.Application
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorActivity
import com.udacity.project4.utils.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@LargeTest
class RemindersActivityTest : AutoCloseKoinTest() {
    private lateinit var context: Application
    private lateinit var reminderDataSource: ReminderDataSource
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(RemindersActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_BACKGROUND_LOCATION"
        )

    @Before
    fun setupApp() {
        stopKoin()
        context = ApplicationProvider.getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    context,
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    context,
                    get() as ReminderDataSource
                )
            }
            single<ReminderDataSource> { RemindersLocalRepository(get()) }
            single { LocalDB.createRemindersDao(context) }
        }

        //register idling resources
        IdlingRegistry.getInstance().apply {
            register(EspressoIdlingResource.countingIdlingResource)
            register(dataBindingIdlingResource)
        }
        // Declare a new koin module.
        startKoin {
            androidContext(context)
            modules(listOf(myModule))
        }

        // Get our real repository.
        reminderDataSource = get()

        // Clear the data to start fresh.
        runBlocking {
            reminderDataSource.deleteAllReminders()
        }
    }

    @Test
    fun openReminderActivityAndAddOneReminder() {
        dataBindingIdlingResource.monitorActivity(mActivityScenarioRule.scenario)
        onView(withId(R.id.addReminderFAB)).perform(ViewActions.click())
        
        onView(withId(R.id.saveReminder)).perform(ViewActions.click())
        onView(withText(context.getString(R.string.add_title))).check(
            matches(
                withEffectiveVisibility(Visibility.VISIBLE)
            )
        )

        onView(withId(R.id.reminderTitle)).perform(
            ViewActions.replaceText("my reminder"),
            ViewActions.closeSoftKeyboard()
        )

        val appCompatEditText3 = Espresso.onView(
            withId(R.id.reminderDescription)
        )
        appCompatEditText3.perform(
            ViewActions.replaceText("buy milk"),
            ViewActions.closeSoftKeyboard()
        )

        val appCompatTextView = Espresso.onView(withId(R.id.selectLocation))
        appCompatTextView.perform(ViewActions.click())

        //adding mark to the map
        onView(withContentDescription("Google Map")).perform(click())

        val appCompatButton = Espresso.onView(withId(R.id.map_button))
        appCompatButton.perform(ViewActions.click())

        val floatingActionButton3 = Espresso.onView(withId(R.id.saveReminder))
        floatingActionButton3.perform(ViewActions.click())
    }

    @Test
    fun testIfItemsAreShownThere() {
//        adding data
        val data1 = ReminderDTO("home reminder", "my home", "picked PIPE", 10.9, 54.5)
        val data2 = ReminderDTO("home reminder 2", "my home 2", "picked PIPE 2", 10.92, 54.52)
        runBlocking { reminderDataSource.saveReminder(data1);reminderDataSource.saveReminder(data2) }

        //watch my repo
        val scenario = ActivityScenario.launch(RemindersActivity::class.java)
        dataBindingIdlingResource.monitorActivity(scenario)

        //check if the data is there and shown for data1
        onView(withText(data1.title)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(data1.description)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(data1.location)).check(ViewAssertions.matches(isDisplayed()))

        //check if the data is there and shown for data2
        onView(withText(data2.title)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(data2.description)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(data2.location)).check(ViewAssertions.matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    @After
    fun unregisterIdlingResources() {
        IdlingRegistry.getInstance().apply {
            unregister(EspressoIdlingResource.countingIdlingResource)
            unregister(dataBindingIdlingResource)
        }
    }
}