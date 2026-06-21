package com.kronos.infra.persistence.repository

import com.kronos.infra.persistence.ScheduleEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID

interface ScheduleJpaRepository : JpaRepository<ScheduleEntity, UUID> {
    fun findByStartTimeBetween(start: LocalDateTime, end: LocalDateTime): List<ScheduleEntity>
}
