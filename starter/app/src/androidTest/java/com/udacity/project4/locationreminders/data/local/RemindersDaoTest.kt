package com.udacity.project4.locationreminders.data.local

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import java.util.concurrent.Executors


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
// Unit test the DAO
@SmallTest
class RemindersDaoTest : AutoCloseKoinTest() {
    private lateinit var remindersDatabase: RemindersDatabase
    private lateinit var remindersDao: RemindersDao
    private lateinit var remindersLocalRepository: RemindersLocalRepository

    private lateinit var data1: ReminderDTO
    private lateinit var dao1: RemindersDao

    lateinit var appContext: Application
    lateinit var localedao: RemindersDao

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDataBase() {
        stopKoin()
        val context = ApplicationProvider.getApplicationContext<Context>()

        val appModule = module { // Room Database instance
            single {
                remindersDatabase = Room.inMemoryDatabaseBuilder(
                    context, RemindersDatabase::class.java
                ).setTransactionExecutor(Executors.newSingleThreadExecutor()).build()
            }
        }

        startKoin {
            androidLogger()
            androidContext(context)
            modules(listOf(appModule))
        }

        remindersDatabase = get() as RemindersDatabase // is wrong ?
//        can I just stop using koin ?
//    but if I am using koin or not THE UI TESTING complain I don't know what to do'

    }

    @After
    fun after() {
        remindersDatabase.close()
    }

    @Test
    fun testDataBase_getDataById() {
        //Given
        //UpThere
        data1 = ReminderDTO("title", "description", "Location", 10.5, 5.5)
//        remindersLocalRepository.saveReminder(data1)

        //When
//        val dataObtained = remindersDatabase.reminderDao().getReminderById(data1.id)
//        val dataObtained = remindersLocalRepository.getReminder(id = data1.id)

        //Then
//        assertThat(dataObtained as ReminderDTO, notNullValue())
//        assertThat(dataObtained.id, `is`(data1.id))
//        assertThat(dataObtained.title, `is`(data1.title))
//        assertThat(dataObtained.description, `is`(data1.description))
//        assertThat(dataObtained.latitude, `is`(data1.latitude))
//        assertThat(dataObtained.longitude, `is`(data1.longitude))
    }
/*
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
 */
}
