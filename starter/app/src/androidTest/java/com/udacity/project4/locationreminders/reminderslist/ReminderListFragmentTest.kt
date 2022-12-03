package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.get
import org.koin.java.KoinJavaComponent.inject

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {

    private lateinit var reminderDataSource: ReminderDataSource
    private lateinit var context: Application

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

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
            single { RemindersLocalRepository(get()) }
            single { LocalDB.createRemindersDao(context) }
        }

        // Declare a new koin module.
        startKoin {
            modules(listOf(myModule))
        }


        // Get our real repository.
        reminderDataSource = get()

        // Clear the data to start fresh.
        runBlocking {
            reminderDataSource.deleteAllReminders()
        }

        IdlingRegistry.getInstance().apply {
            register(EspressoIdlingResource.countingIdlingResource)
            register(dataBindingIdlingResource)
        }
    }

    @Test
    fun firstTest() {


        val scenario = launchFragmentInContainer<ReminderListFragment>()
        dataBindingIdlingResource.monitorFragment(scenario)

        //pleaaaaaaaaaaaaaaaaase work

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