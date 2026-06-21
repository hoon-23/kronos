package com.kronos.application.schedule.port.inbound

import com.kronos.domain.schedule.Schedule
import java.time.LocalDate
import java.util.UUID

interface GetScheduleUseCase {
    fun getById(id: UUID): Schedule
    fun getByDate(date: LocalDate): List<Schedule>
    fun getAll(): List<Schedule>
    fun getConflicts(): List<Schedule>
}
