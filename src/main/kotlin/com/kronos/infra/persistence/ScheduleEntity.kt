package com.kronos.infra.persistence

import com.kronos.domain.schedule.Schedule
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "schedules")
class ScheduleEntity(
    @Id
    val id: UUID,

    @Column(nullable = false)
    val title: String,

    @Column
    val description: String?,

    @Column(nullable = false)
    val startTime: LocalDateTime,

    @Column(nullable = false)
    val endTime: LocalDateTime,

    @ElementCollection(fetch = FetchType.EAGER)
    val participants: List<String> = emptyList(),
) {
    fun toDomain() = Schedule(
        id = id,
        title = title,
        description = description,
        startTime = startTime,
        endTime = endTime,
        participants = participants,
    )

    companion object {
        fun from(schedule: Schedule) = ScheduleEntity(
            id = schedule.id,
            title = schedule.title,
            description = schedule.description,
            startTime = schedule.startTime,
            endTime = schedule.endTime,
            participants = schedule.participants,
        )
    }
}
