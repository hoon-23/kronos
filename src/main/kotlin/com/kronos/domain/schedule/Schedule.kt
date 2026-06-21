package com.kronos.domain.schedule

import java.time.LocalDateTime
import java.util.UUID

data class Schedule(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val description: String? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val participants: List<String> = emptyList(),
) {
    fun conflictsWith(other: Schedule): Boolean =
        startTime < other.endTime && endTime > other.startTime
}
