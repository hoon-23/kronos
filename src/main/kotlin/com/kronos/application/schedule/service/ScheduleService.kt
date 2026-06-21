package com.kronos.application.schedule.service

import com.kronos.application.schedule.port.inbound.CreateScheduleCommand
import com.kronos.application.schedule.port.inbound.CreateScheduleUseCase
import com.kronos.application.schedule.port.inbound.DeleteScheduleUseCase
import com.kronos.application.schedule.port.inbound.GetScheduleUseCase
import com.kronos.application.schedule.port.inbound.UpdateScheduleCommand
import com.kronos.application.schedule.port.inbound.UpdateScheduleUseCase
import com.kronos.application.schedule.port.outbound.DeleteSchedulePort
import com.kronos.application.schedule.port.outbound.LoadSchedulePort
import com.kronos.application.schedule.port.outbound.SaveSchedulePort
import com.kronos.domain.schedule.Schedule
import com.kronos.domain.schedule.ScheduleValidator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Service
@Transactional
class ScheduleService(
    private val saveSchedulePort: SaveSchedulePort,
    private val loadSchedulePort: LoadSchedulePort,
    private val deleteSchedulePort: DeleteSchedulePort,
) : CreateScheduleUseCase, GetScheduleUseCase, UpdateScheduleUseCase, DeleteScheduleUseCase {

    override fun create(command: CreateScheduleCommand): Schedule {
        val schedule = Schedule(
            title = command.title,
            description = command.description,
            startTime = command.startTime,
            endTime = command.endTime,
            participants = command.participants,
        )
        ScheduleValidator.validate(schedule)
        return saveSchedulePort.save(schedule)
    }

    @Transactional(readOnly = true)
    override fun getById(id: UUID): Schedule =
        loadSchedulePort.findById(id) ?: throw NoSuchElementException("일정을 찾을 수 없습니다: $id")

    @Transactional(readOnly = true)
    override fun getByDate(date: LocalDate): List<Schedule> =
        loadSchedulePort.findByDate(date)

    @Transactional(readOnly = true)
    override fun getAll(): List<Schedule> =
        loadSchedulePort.findAll()

    @Transactional(readOnly = true)
    override fun getConflicts(): List<Schedule> {
        val all = loadSchedulePort.findAll()
        return all.filter { schedule ->
            all.any { other -> other.id != schedule.id && schedule.conflictsWith(other) }
        }
    }

    override fun update(command: UpdateScheduleCommand): Schedule {
        val existing = loadSchedulePort.findById(command.id)
            ?: throw NoSuchElementException("일정을 찾을 수 없습니다: ${command.id}")
        val updated = existing.copy(
            title = command.title,
            description = command.description,
            startTime = command.startTime,
            endTime = command.endTime,
            participants = command.participants,
        )
        ScheduleValidator.validate(updated)
        return saveSchedulePort.save(updated)
    }

    override fun delete(id: UUID) {
        loadSchedulePort.findById(id) ?: throw NoSuchElementException("일정을 찾을 수 없습니다: $id")
        deleteSchedulePort.delete(id)
    }
}
