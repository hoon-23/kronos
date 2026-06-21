package com.kronos.application.schedule.port.inbound

import com.kronos.domain.schedule.Schedule
import java.time.LocalDateTime

interface CreateScheduleUseCase {
    fun create(command: CreateScheduleCommand): Schedule
}

data class CreateScheduleCommand(
    val title: String,
    val description: String? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val participants: List<String> = emptyList(),
)
