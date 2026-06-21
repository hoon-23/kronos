package com.kronos.domain.schedule

import java.util.UUID

sealed class ScheduleException(message: String) : RuntimeException(message)

class ScheduleNotFoundException(id: UUID) :
    ScheduleException("일정을 찾을 수 없습니다: $id")

class ScheduleConflictException(title: String, conflictingTitle: String) :
    ScheduleException("'$title' 일정이 '$conflictingTitle' 일정과 시간이 겹칩니다.")

class InvalidScheduleException(reason: String) :
    ScheduleException(reason)

class NaturalLanguageParseException(cause: Throwable) :
    ScheduleException("자연어 파싱에 실패했습니다: ${cause.message}")
