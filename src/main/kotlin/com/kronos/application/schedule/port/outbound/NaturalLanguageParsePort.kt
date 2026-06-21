package com.kronos.application.schedule.port.outbound

import com.kronos.application.schedule.port.inbound.CreateScheduleCommand

interface NaturalLanguageParsePort {
    fun parse(text: String): CreateScheduleCommand
}
