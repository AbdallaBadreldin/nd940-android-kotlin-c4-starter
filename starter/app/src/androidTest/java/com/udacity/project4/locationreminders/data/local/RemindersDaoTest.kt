package com.udacity.project4.locationreminders.data.local

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import java.util.concurrent.Executors


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
// Unit test the DAO
@SmallTest
class RemindersDaoTest {
    private lateinit var remindersDatabase: RemindersDatabase
    private lateinit var remindersDao: RemindersDao
//    private lateinit var remindersLocalRepository: RemindersLocalRepository

    private lateinit var data1: ReminderDTO
//    private lateinit var dao1: RemindersDao

    lateinit var appContext: Application
//    lateinit var localedao: RemindersDao

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDataBase() {
        stopKoin()
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Room Database instance
        remindersDatabase = Room.inMemoryDatabaseBuilder(
            context, RemindersDatabase::class.java
        ).setTransactionExecutor(Executors.newSingleThreadExecutor()).build()

        remindersDao = remindersDatabase.reminderDao()

    }

    @Test
    fun testDataBase_getDataById() {
        runTest {
            //Given
            //UpThere
            data1 = ReminderDTO("title", "description", "Location", 10.5, 5.5)
            val data2 = ReminderDTO("title", "description", "Location", 10.5, 5.5)
            remindersDao.saveReminder(data1)
            remindersDao.saveReminder(data2)

            // When
            // val dataObtained = remindersDatabase.reminderDao().getReminderById(data1.id)
            val dataObtained = remindersDao.getReminderById(data1.id)

            //Then
            assertThat(dataObtained as ReminderDTO, notNullValue())
            assertThat(dataObtained.id, `is`(data1.id))
            assertThat(dataObtained.title, `is`(data1.title))
            assertThat(dataObtained.description, `is`(data1.description))
            assertThat(dataObtained.latitude, `is`(data1.latitude))
            assertThat(dataObtained.longitude, `is`(data1.longitude))
        }
    }

    @Test
    fun testDataBase_clearAllData() = runTest {
        //Given
        // remindersDatabase.reminderDao().deleteAllReminders()
        data1 = ReminderDTO("title", "description", "Location", 10.5, 5.5)
        remindersDao.saveReminder(data1)
        remindersDao.deleteAllReminders()
        //When
        // val dataObtained = remindersDatabase.reminderDao().getReminders()
        val dataObtained = remindersDao.getReminders()

        //Then
        assertThat(dataObtained, notNullValue())
        assertThat(dataObtained, `is`(arrayListOf()))
        //   finally it works thank god
    }

    @After
    fun terminateDataBase() {
        remindersDatabase.close()
    }

}
