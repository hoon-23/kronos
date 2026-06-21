package com.kronos.infra.ai

import com.kronos.application.schedule.port.inbound.CreateScheduleCommand
import org.springframework.stereotype.Component

@Component
class ClaudeAdapter {

    fun parseNaturalLanguage(text: String): CreateScheduleCommand {
        TODO("Claude API 연동 구현 예정")
    }
}
