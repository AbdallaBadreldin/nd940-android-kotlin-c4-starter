package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(
    private var list: MutableList<ReminderDTO>,
    private var isReturnErrors: Boolean
) : ReminderDataSource {

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return if (!isReturnErrors)
            Result.Success<List<ReminderDTO>>(list)
        else {
            Result.Error("get reminders failed!")
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        list.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (!isReturnErrors) {
            list.firstOrNull { it.id == id }?.let { return Result.Success(it) }
            return Result.Error("Reminder not found!")
        } else
            return Result.Error("get reminder failed!")
    }

    override suspend fun deleteAllReminders() {
            list.clear()
    }


    fun setIsReturnError(isReturnError: Boolean) {
        this.isReturnErrors = isReturnError
    }

    fun setList(list: MutableList<ReminderDTO>) {
        this.list = list
    }
}