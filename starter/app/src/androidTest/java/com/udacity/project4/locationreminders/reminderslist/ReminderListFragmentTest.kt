package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.udacity.project4.R
import com.udacity.project4.databinding.FragmentRemindersBinding
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.EspressoIdlingResource
import com.udacity.project4.util.monitorActivity
import com.udacity.project4.util.monitorFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.Mockito
import kotlin.test.assertTrue


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest : KoinTest {

    private lateinit var context: Application

    private val dataBindingIdlingResource = DataBindingIdlingResource()


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
            single { RemindersLocalRepository(get()) }
            single { LocalDB.createRemindersDao(context) }
        }

        // Declare a new koin module.
        startKoin {
//            androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
            modules(listOf(myModule))
        }

    }

    @Before
    fun registerIdlingResources() {
        IdlingRegistry.getInstance().apply {
            register(EspressoIdlingResource.countingIdlingResource)
            register(dataBindingIdlingResource)
        }
    }


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

    @Test
    fun remindersActivityTest3() {
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(scenario)


        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        val floatingActionButton = Espresso.onView(
            Matchers.allOf(
                withId(R.id.addReminderFAB),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.refreshLayout),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(ViewActions.click())


        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.waitForIdle()
        assertTrue(device.hasObject(By.text("add title")))

        val appCompatEditText = Espresso.onView(
            Matchers.allOf(
                withId(R.id.reminderTitle),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(
            ViewActions.replaceText("my reminder"),
            ViewActions.closeSoftKeyboard()
        )

        Espresso.pressBack()

        val floatingActionButton2 = Espresso.onView(
            Matchers.allOf(
                withId(R.id.saveReminder),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        floatingActionButton2.perform(ViewActions.click())

        val appCompatEditText2 = Espresso.onView(
            Matchers.allOf(
                withId(R.id.reminderDescription),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(ViewActions.click())

        val appCompatEditText3 = Espresso.onView(
            Matchers.allOf(
                withId(R.id.reminderDescription),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(
            ViewActions.replaceText("buy milk"),
            ViewActions.closeSoftKeyboard()
        )

        val appCompatTextView = Espresso.onView(
            Matchers.allOf(
                withId(R.id.selectLocation), withText("Reminder Location"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatTextView.perform(ViewActions.click())

        val appCompatButton = Espresso.onView(
            Matchers.allOf(
                withId(R.id.map_button), withText("Confirm"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(ViewActions.click())

        val floatingActionButton3 = Espresso.onView(
            Matchers.allOf(
                withId(R.id.saveReminder),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
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
        stopKoin()
        IdlingRegistry.getInstance().apply {
            unregister(EspressoIdlingResource.countingIdlingResource)
            unregister(dataBindingIdlingResource)
        }
    }
}