package com.kronos.application.schedule.port.inbound

import java.util.UUID

interface DeleteScheduleUseCase {
    fun delete(id: UUID)
}
