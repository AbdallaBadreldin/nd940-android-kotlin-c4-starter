package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
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

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
// Unit test the DAO
@SmallTest
class RemindersDaoTest {
    private lateinit var remindersDatabase: RemindersDatabase
    private lateinit var data1: ReminderDTO
    private lateinit var dao1: RemindersDao

    //(DONE)Todo:// test database
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDataBase() {
        remindersDatabase = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
        dao1 = remindersDatabase.reminderDao()
        runTest {
            //add data to database
            data1 = ReminderDTO("title", "description", "Location", 10.5, 5.5)
            remindersDatabase.reminderDao().saveReminder(data1)
            dao1.saveReminder(data1)
        }
    }

    @Test
    fun testDataBase_getDataById() = runTest {
        //Given
        //UpThere
        data1  = ReminderDTO("title", "description", "Location", 10.5, 5.5)
        dao1.saveReminder(data1)

        //When
//        val dataObtained = remindersDatabase.reminderDao().getReminderById(data1.id)
        val dataObtained =dao1.getReminderById(data1.id)

        //Then
        assertThat(dataObtained as ReminderDTO, notNullValue())
        assertThat(dataObtained.id, `is`(data1.id))
        assertThat(dataObtained.title, `is`(data1.title))
        assertThat(dataObtained.description, `is`(data1.description))
        assertThat(dataObtained.latitude, `is`(data1.latitude))
        assertThat(dataObtained.longitude, `is`(data1.longitude))
    }

    @Test
    fun testDataBase_clearAllData() = runTest {
        //Given
//        remindersDatabase.reminderDao().deleteAllReminders()
        data1  = ReminderDTO("title", "description", "Location", 10.5, 5.5)
        dao1.saveReminder(data1)
        dao1.deleteAllReminders()
        //When
//        val dataObtained = remindersDatabase.reminderDao().getReminders()
        val dataObtained =dao1.getReminders()

        //Then
        assertThat(dataObtained as ReminderDTO, notNullValue())
        assertThat(dataObtained, `is`(emptyList<ReminderDTO>()))
    }

    @After
    fun terminateDataBase() {
        remindersDatabase.close()
    }
}