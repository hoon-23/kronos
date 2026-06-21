package com.kronos.infra.web.dto

import com.kronos.application.schedule.port.inbound.CreateScheduleCommand
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class CreateScheduleRequest(
    @field:NotBlank val title: String,
    val description: String? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val participants: List<String> = emptyList(),
) {
    fun toCommand() = CreateScheduleCommand(
        title = title,
        description = description,
        startTime = startTime,
        endTime = endTime,
        participants = participants,
    )
}
