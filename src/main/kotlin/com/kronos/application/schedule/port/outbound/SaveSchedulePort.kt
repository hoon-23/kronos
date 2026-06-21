package com.kronos.application.schedule.port.outbound

import com.kronos.domain.schedule.Schedule

interface SaveSchedulePort {
    fun save(schedule: Schedule): Schedule
}
