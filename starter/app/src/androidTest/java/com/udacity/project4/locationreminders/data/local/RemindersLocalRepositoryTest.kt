package com.udacity.project4.locationreminders.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import java.io.IOException
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {
    //  (DONE)  TODO: Add testing implementation to the RemindersLocalRepository.kt
    private lateinit var repository: RemindersLocalRepository
    private lateinit var database: RemindersDatabase
    private lateinit var dao: RemindersDao

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
         val context = ApplicationProvider.getApplicationContext<Context>()
         database = Room.inMemoryDatabaseBuilder(
             context, RemindersDatabase::class.java
         ).setTransactionExecutor(Executors.newSingleThreadExecutor()).build()
         dao = database.reminderDao()

        repository = RemindersLocalRepository(dao, ioDispatcher = Dispatchers.Main)
//     It also stops working just tried every thing even wrong answers
    }

    @After
    fun closeDb() {
        stopKoin()
        database.close()
    }

    @Test
     fun testRoom() {
        runTest {
            //given
            val newTask = ReminderDTO("title", "description", "22.8745, 88.6971", 22.8745, 88.6971)
            dao.saveReminder(newTask)
            dao.saveReminder(newTask)
            dao.saveReminder(newTask)

            //when
            val byId = dao.getReminderById(newTask.id)

            //then
            ViewMatchers.assertThat(byId?.id, CoreMatchers.`is`(newTask.id))
            ViewMatchers.assertThat(byId?.title, CoreMatchers.`is`(newTask.title))
            ViewMatchers.assertThat(byId?.location, CoreMatchers.`is`(newTask.location))
            ViewMatchers.assertThat(byId?.description, CoreMatchers.`is`(newTask.description))
            ViewMatchers.assertThat(byId?.latitude, CoreMatchers.`is`(newTask.latitude))
            ViewMatchers.assertThat(byId?.longitude, CoreMatchers.`is`(newTask.longitude))
        }
    }

    @Test
    fun saveReminder_retrievesReminder() {
        runBlocking {
            //Given
            val newTask = ReminderDTO("title", "description", "22.8745, 88.6971", 22.8745, 88.6971)
            repository.saveReminder(newTask)

            //When
            val result = repository.getReminder(newTask.id)

            //Then
            result as Result.Success
            ViewMatchers.assertThat(result.data.title, CoreMatchers.`is`(newTask.title))
            ViewMatchers.assertThat(result.data.id, CoreMatchers.`is`(newTask.id))
            ViewMatchers.assertThat(result.data.description, CoreMatchers.`is`(newTask.description))
            ViewMatchers.assertThat(result.data.location, CoreMatchers.`is`(newTask.location))
            ViewMatchers.assertThat(result.data.latitude, CoreMatchers.`is`(newTask.latitude))
            ViewMatchers.assertThat(result.data.longitude, CoreMatchers.`is`(newTask.longitude))
        }
    }

    @Test
    fun completeReminder_retrievedReminderIsComplete() {
        runTest {
            //Given
            val newTask = ReminderDTO("title", "description", "10.5, 11.5", 10.5, 11.5)
            repository.saveReminder(newTask)

            //When
            val result = repository.getReminder(newTask.id)

            //Then
            result as Result.Success
            ViewMatchers.assertThat(result.data.title, CoreMatchers.`is`(newTask.title))
        }
    }

    @Test
    fun errorReminder_retrievesReminder() {
        runTest {
            //Given
            val newTask = ReminderDTO("title", "description", "10.5, 11.5", 10.5, 11.5)

            //When
            val result = repository.getReminder(newTask.id)

            //Then
            result as Result.Error
            ViewMatchers.assertThat(result.message, CoreMatchers.`is`("Reminder not found!"))
        }
    }

}