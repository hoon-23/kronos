package com.kronos.domain.schedule

import java.time.LocalDateTime

object ScheduleValidator {

    fun validate(schedule: Schedule) {
        if (schedule.title.isBlank()) throw InvalidScheduleException("제목은 비어있을 수 없습니다.")
        if (!schedule.startTime.isBefore(schedule.endTime)) throw InvalidScheduleException("시작 시간은 종료 시간보다 이전이어야 합니다.")
        if (!schedule.startTime.isAfter(LocalDateTime.now())) throw InvalidScheduleException("시작 시간은 현재 시간 이후여야 합니다.")
    }
}
