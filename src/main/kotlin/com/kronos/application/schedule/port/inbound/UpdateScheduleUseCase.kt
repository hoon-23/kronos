package com.kronos.application.schedule.port.inbound

import com.kronos.domain.schedule.Schedule
import java.time.LocalDateTime
import java.util.UUID

interface UpdateScheduleUseCase {
    fun update(command: UpdateScheduleCommand): Schedule
}

data class UpdateScheduleCommand(
    val id: UUID,
    val title: String,
    val description: String? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val participants: List<String> = emptyList(),
)
