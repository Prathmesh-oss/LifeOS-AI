package com.lifeosai.app.data.repository

import com.lifeosai.app.data.dao.CalendarDao
import com.lifeosai.app.data.entity.CalendarEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepositoryImpl @Inject constructor(
    private val calendarDao: CalendarDao
) {
    fun getEvents(start: Long, end: Long): Flow<List<CalendarEntity>> {
        return calendarDao.getEventsInRange(start, end)
    }

    suspend fun saveEvent(event: CalendarEntity) {
        calendarDao.insertEvent(event)
    }

    suspend fun deleteEvent(eventId: String) {
        calendarDao.deleteEvent(eventId)
    }
}
