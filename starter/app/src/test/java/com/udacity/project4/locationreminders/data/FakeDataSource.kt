package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(
    private var list: MutableList<ReminderDTO>,
    private var isReturnErrors: Boolean
) :
    ReminderDataSource {

    override suspend fun getReminders(): Result<List<ReminderDTO>> = withContext(Dispatchers.IO) {
        try {
            if (!isReturnErrors)
                return@withContext Result.Success<List<ReminderDTO>>(list)
            else {
                return@withContext Result.Error("Error")
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        withContext(Dispatchers.IO) {
            try {
                list.add(reminder)
            } catch (e: java.lang.Exception) {
                //  return@withContext Result.Error(e.localizedMessage)
                //  caught Exception
            }
        }
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> =
        withContext(Dispatchers.IO) {
            try {
                if (!isReturnErrors) {
                    list.first { it.id == id }.let { return@withContext Result.Success(it) }
                } else
                    return@withContext Result.Error("Reminder not found!")
            } catch (e: java.lang.Exception) {
                return@withContext Result.Error(e.localizedMessage)
            }
        }

    override suspend fun deleteAllReminders() {
        try {
            list.clear()
        } catch (e: java.lang.Exception) {
            //caught something wrong
        }
    }


    fun setIsReturnError(isReturnError: Boolean) {
        this.isReturnErrors = isReturnError
    }

    fun setList(list: MutableList<ReminderDTO>) {
        this.list = list
    }
}