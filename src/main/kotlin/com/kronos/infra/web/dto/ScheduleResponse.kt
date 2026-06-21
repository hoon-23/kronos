package com.kronos.infra.web.dto

import com.kronos.domain.schedule.Schedule
import java.time.LocalDateTime
import java.util.UUID

data class ScheduleResponse(
    val id: UUID,
    val title: String,
    val description: String?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val participants: List<String>,
) {
    companion object {
        fun from(schedule: Schedule) = ScheduleResponse(
            id = schedule.id,
            title = schedule.title,
            description = schedule.description,
            startTime = schedule.startTime,
            endTime = schedule.endTime,
            participants = schedule.participants,
        )
    }
}
