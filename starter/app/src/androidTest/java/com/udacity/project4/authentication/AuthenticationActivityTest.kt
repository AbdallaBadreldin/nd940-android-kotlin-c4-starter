package com.udacity.project4.authentication


import android.app.Application
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.util.DataBindingIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@LargeTest
@RunWith(AndroidJUnit4::class)
class AuthenticationActivityTest {

    // An idling resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()

//    @Rule
//    var activityScenarioRule: ActivityScenarioRule<RemindersActivity> =
//        ActivityScenarioRule<RemindersActivity>(
//            RemindersActivity::class.java
//        )
//
//    @Before
//    fun registerIdlingResource() {
//        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
//        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
//    }
//
//    @After
//    fun unregisterIdlingResource() {
//        if (dataBindingIdlingResource != null) {
//            IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
//            IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
//        }
//    }

    @Rule
    var activityScenarioRule: ActivityScenarioRule<AuthenticationActivity> = ActivityScenarioRule(
        AuthenticationActivity::class.java
    )

    @Test
    fun authenticationActivityTest() {

        // Type text and then press the button.
//        onView(withId(R.id.))
//            .perform(
//                typeText(com.example.android.testing.espresso.BasicSample.ChangeTextBehaviorTest.STRING_TO_BE_TYPED),
//                closeSoftKeyboard()
//            )
//        val activityScenario =
//            ActivityScenario.launch(AuthenticationActivity::class.java)
//        dataBindingIdlingResource.monitorActivity(activityScenario)
//        activityScenario.onActivity(Action)
//        Thread.sleep(1000)
//        val scenario = ActivityScenario.launch(AuthenticationActivity::class.java)
//        DataBindingIdlingResource().monitorActivity(activityScenario as ActivityScenario<out FragmentActivity>)
//        IdlingRegistry.getInstance().register(mIdlingResource);
//        Thread.sleep(1000)
//        val view = onView(
//            allOf(
//                withId(android.R.id.statusBarBackground),
//                withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java)),
//                isDisplayed()
//            )
//        )
//        Thread.sleep(1000)
//        view.check(matches(isDisplayed()))
//        Thread.sleep(1000)
//        val materialButton = onView(
//            allOf(
//                withId(com.firebase.ui.auth.R.id.email_button), withText("Sign in with email"),
//                childAtPosition(
//                    allOf(
//                        withId(com.firebase.ui.auth.R.id.btn_holder),
//                        childAtPosition(
//                            withId(com.google.android.material.R.id.container),
//                            0
//                        )
//                    ),
//                    0
//                )
//            )
//        )
//        Thread.sleep(1000)
//        materialButton.perform(scrollTo(), click())
//        Thread.sleep(1000)
//        val textInputEditText = onView(
//            allOf(
//                withId(com.firebase.ui.auth.R.id.email),
//                childAtPosition(
//                    childAtPosition(
//                        withId(com.firebase.ui.auth.R.id.email_layout),
//                        0
//                    ),
//                    0
//                )
//            )
//        )
//        textInputEditText.perform(scrollTo(), replaceText("a@a.com"), closeSoftKeyboard())
//
//        val materialButton2 = onView(
//            allOf(
//                withId(com.firebase.ui.auth.R.id.button_next), withText("Next"),
//                childAtPosition(
//                    childAtPosition(
//                        withClassName(`is`("android.widget.ScrollView")),
//                        0
//                    ),
//                    2
//                )
//            )
//        )
//        materialButton2.perform(scrollTo(), click())
//
//        val textInputEditText2 = onView(
//            allOf(
//                withId(com.firebase.ui.auth.R.id.name),
//                childAtPosition(
//                    childAtPosition(
//                        withId(com.firebase.ui.auth.R.id.name_layout),
//                        0
//                    ),
//                    0
//                )
//            )
//        )
//        textInputEditText2.perform(scrollTo(), click())
//
//        val textInputEditText3 = onView(
//            allOf(
//                withId(com.firebase.ui.auth.R.id.name),
//                childAtPosition(
//                    childAtPosition(
//                        withId(com.firebase.ui.auth.R.id.name_layout),
//                        0
//                    ),
//                    0
//                )
//            )
//        )
//        textInputEditText3.perform(scrollTo(), click())
//
//        val textInputEditText4 = onView(
//            allOf(
//                withId(com.firebase.ui.auth.R.id.name),
//                childAtPosition(
//                    childAtPosition(
//                        withId(com.firebase.ui.auth.R.id.name_layout),
//                        0
//                    ),
//                    0
//                )
//            )
//        )
//        textInputEditText4.perform(scrollTo(), replaceText("12"), closeSoftKeyboard())
//
//        val textInputEditText5 = onView(
//            allOf(
//                withId(com.firebase.ui.auth.R.id.password),
//                childAtPosition(
//                    childAtPosition(
//                        withId(com.firebase.ui.auth.R.id.password_layout),
//                        0
//                    ),
//                    0
//                )
//            )
//        )
//        textInputEditText5.perform(scrollTo(), click())
//
//        val textInputEditText6 = onView(
//            allOf(
//                withId(com.firebase.ui.auth.R.id.password),
//                childAtPosition(
//                    childAtPosition(
//                        withId(com.firebase.ui.auth.R.id.password_layout),
//                        0
//                    ),
//                    0
//                )
//            )
//        )
//        textInputEditText6.perform(scrollTo(), click())
//
//        val textInputEditText7 = onView(
//            allOf(
//                withId(com.firebase.ui.auth.R.id.password),
//                childAtPosition(
//                    childAtPosition(
//                        withId(com.firebase.ui.auth.R.id.password_layout),
//                        0
//                    ),
//                    0
//                )
//            )
//        )
//        textInputEditText7.perform(scrollTo(), replaceText("123456"), closeSoftKeyboard())
//
//        val checkableImageButton = onView(
//            allOf(
//                withId(com.google.android.material.R.id.text_input_end_icon),
//                withContentDescription("Show password"),
//                childAtPosition(
//                    childAtPosition(
//                        withClassName(`is`("com.google.android.material.textfield.EndCompoundLayout")),
//                        1
//                    ),
//                    0
//                ),
//                isDisplayed()
//            )
//        )
//        checkableImageButton.perform(click())
//
//        val materialButton3 = onView(
//            allOf(
//                withId(com.firebase.ui.auth.R.id.button_create), withText("Save"),
//                childAtPosition(
//                    childAtPosition(
//                        withClassName(`is`("android.widget.ScrollView")),
//                        0
//                    ),
//                    3
//                )
//            )
//        )
//        materialButton3.perform(scrollTo(), click())
////        scenario.close()
//        activityScenario.close()
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

    @Before
    fun initKoin() {
        stopKoin()
//        val   instrumentationContext = InstrumentationRegistry.getInstrumentation().context
        val instrumentationContext = ApplicationProvider.getApplicationContext<Application>()
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            viewModel {
                RemindersListViewModel(
                    get(),
                    get() as ReminderDataSource
                )
            }
            //Declare singleton definitions to be later injected using by inject()
            single {
                //This view model is declared singleton to be used across multiple fragments
                SaveReminderViewModel(
                    get(),
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) }
            single { LocalDB.createRemindersDao(instrumentationContext) }
        }

        startKoin {
//            androidContext(instrumentationContext)
            modules(listOf(myModule))
        }
    }

}
