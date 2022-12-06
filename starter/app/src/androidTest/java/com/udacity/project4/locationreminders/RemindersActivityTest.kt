package com.udacity.project4.locationreminders

import android.app.Application
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorActivity
import com.udacity.project4.utils.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import java.util.regex.Pattern.matches


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@LargeTest
class RemindersActivityTest : AutoCloseKoinTest() {
    private lateinit var context: Application

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
            single<ReminderDataSource>{ RemindersLocalRepository(get() ) }
            single { LocalDB.createRemindersDao(context) }
        }
        IdlingRegistry.getInstance().apply {
            register(EspressoIdlingResource.countingIdlingResource)
            register(dataBindingIdlingResource)
        }
        // Declare a new koin module.
        startKoin {
            androidContext(context)
            modules(listOf(myModule))
        }
    }

    @Test
    fun remindersActivityTest3() {
        dataBindingIdlingResource.monitorActivity(mActivityScenarioRule.scenario)
        onView(withId(R.id.addReminderFAB)).perform(ViewActions.click())

//        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
//        device.waitForIdle()
//        assertTrue(device.hasObject(By.text("add title")))

        onView(withId(R.id.reminderTitle)).perform(
            ViewActions.replaceText("my reminder"),
            ViewActions.closeSoftKeyboard()
        )
//        Espresso.closeSoftKeyboard()
//        Espresso.pressBack()
        onView(withId(R.id.saveReminder)).perform(ViewActions.click())

//        onView(withText(context.getString(R.string.add_title))).check(m)
//        onView(withId(R.id.reminderDescription)).
//        check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        val appCompatEditText3 = Espresso.onView(
            withId(R.id.reminderDescription)
        )
        appCompatEditText3.perform(
            ViewActions.replaceText("buy milk"),
            ViewActions.closeSoftKeyboard()
        )

        val appCompatTextView = Espresso.onView(withId(R.id.selectLocation))
        appCompatTextView.perform(ViewActions.click())

        val appCompatButton = Espresso.onView(withId(R.id.map_button))
        appCompatButton.perform(ViewActions.click())

        val floatingActionButton3 = Espresso.onView(withId(R.id.saveReminder))
        floatingActionButton3.perform(ViewActions.click())
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