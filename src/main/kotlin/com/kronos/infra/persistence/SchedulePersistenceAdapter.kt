package com.kronos.infra.persistence

import com.kronos.application.schedule.port.outbound.DeleteSchedulePort
import com.kronos.application.schedule.port.outbound.LoadSchedulePort
import com.kronos.application.schedule.port.outbound.SaveSchedulePort
import com.kronos.domain.schedule.Schedule
import com.kronos.infra.persistence.mapper.ScheduleMapper
import com.kronos.infra.persistence.repository.ScheduleJpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID

@Component
class SchedulePersistenceAdapter(
    private val repository: ScheduleJpaRepository,
    private val mapper: ScheduleMapper,
) : SaveSchedulePort, LoadSchedulePort, DeleteSchedulePort {

    override fun save(schedule: Schedule): Schedule =
        mapper.toDomain(repository.save(mapper.toEntity(schedule)))

    override fun findById(id: UUID): Schedule? =
        repository.findById(id).map(mapper::toDomain).orElse(null)

    override fun findByDate(date: LocalDate): List<Schedule> =
        repository.findByStartTimeBetween(
            date.atStartOfDay(),
            date.plusDays(1).atStartOfDay(),
        ).map(mapper::toDomain)

    override fun findAll(): List<Schedule> =
        repository.findAll().map(mapper::toDomain)

    override fun delete(id: UUID) =
        repository.deleteById(id)
}
