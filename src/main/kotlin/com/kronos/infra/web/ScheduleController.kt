package com.kronos.infra.web

import com.kronos.application.schedule.port.inbound.CreateScheduleUseCase
import com.kronos.application.schedule.port.inbound.DeleteScheduleUseCase
import com.kronos.application.schedule.port.inbound.GetScheduleUseCase
import com.kronos.application.schedule.port.inbound.UpdateScheduleUseCase
import com.kronos.infra.web.dto.CreateScheduleRequest
import com.kronos.infra.web.dto.ScheduleResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/schedules")
class ScheduleController(
    private val createScheduleUseCase: CreateScheduleUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateScheduleRequest): ScheduleResponse =
        ScheduleResponse.from(createScheduleUseCase.create(request.toCommand()))

    @GetMapping
    fun getAll(): List<ScheduleResponse> =
        getScheduleUseCase.getAll().map(ScheduleResponse::from)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ScheduleResponse =
        ScheduleResponse.from(getScheduleUseCase.getById(id))

    @GetMapping("/conflicts")
    fun getConflicts(): List<ScheduleResponse> =
        getScheduleUseCase.getConflicts().map(ScheduleResponse::from)

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody request: CreateScheduleRequest): ScheduleResponse {
        val command = request.toCommand()
        return ScheduleResponse.from(
            updateScheduleUseCase.update(
                com.kronos.application.schedule.port.inbound.UpdateScheduleCommand(
                    id = id,
                    title = command.title,
                    description = command.description,
                    startTime = command.startTime,
                    endTime = command.endTime,
                    participants = command.participants,
                )
            )
        )
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) =
        deleteScheduleUseCase.delete(id)
}
