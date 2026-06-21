package com.kronos.application.schedule.port.outbound

import java.util.UUID

interface DeleteSchedulePort {
    fun delete(id: UUID)
}
