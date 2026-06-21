package com.kronos.application.schedule.port.inbound

import com.kronos.domain.schedule.Schedule

interface ParseNaturalLanguageUseCase {
    fun createFromNaturalLanguage(text: String): Schedule
}
