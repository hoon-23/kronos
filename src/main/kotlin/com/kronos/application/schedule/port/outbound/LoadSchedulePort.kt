package com.kronos.application.schedule.port.outbound

import com.kronos.domain.schedule.Schedule
import java.time.LocalDate
import java.util.UUID

interface LoadSchedulePort {
    fun findById(id: UUID): Schedule?
    fun findByDate(date: LocalDate): List<Schedule>
    fun findAll(): List<Schedule>
}
