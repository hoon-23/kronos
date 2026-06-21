package com.kronos.infra.persistence.mapper

import com.kronos.domain.schedule.Schedule
import com.kronos.infra.persistence.ScheduleEntity
import org.springframework.stereotype.Component

@Component
class ScheduleMapper {
    fun toDomain(entity: ScheduleEntity): Schedule = entity.toDomain()
    fun toEntity(domain: Schedule): ScheduleEntity = ScheduleEntity.from(domain)
}
